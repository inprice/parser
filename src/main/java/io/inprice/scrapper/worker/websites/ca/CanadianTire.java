package io.inprice.scrapper.worker.websites.ca;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mashape.unirest.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

/**
 * Parser for CanadianTire Canada
 *
 * Please note that: link's url (aka main url) is never used for data pulling
 *
 * This is a very special case. CanadianTire provides too limited data in html
 * form. So, we need to make a json request to collect all the data we need.
 *
 * in getJsonData(), the data is pulled with a payload whose steps are explained
 * above
 *
 * @author mdpinar
 */
public class CanadianTire extends AbstractWebsite {

  public CanadianTire(Link link) {
    super(link);
  }

  /**
   * Returns payload as key value query string
   *
   * @return String - as query parameter payload
   */
  private String getPayload() {
    StringBuilder sb = new StringBuilder();
    sb.append("SKU=");
    sb.append(getSku());
    sb.append("&");
    sb.append("Store=0144");
    sb.append("&");
    sb.append("Banner=CTR");
    sb.append("&");
    sb.append("isKiosk=FALSE");
    sb.append("&");
    sb.append("Language=E");
    sb.append("&");
    sb.append("_");
    sb.append(new Date().getTime());
    return sb.toString();
  }

  /**
   * Request the data with a constant url with product-id and payload. Besides,
   * data handles best-offer which holds some important data
   *
   * @return JSONObject - json
   */
  @Override
  public JSONObject getJsonData() {
    HttpResponse<String> response = httpClient.get("https://www.canadiantire.ca/ESB/PriceAvailability?" + getPayload());
    if (response != null && response.getStatus() < 400) {
      JSONArray data = new JSONArray(response.getBody());
      if (!data.isEmpty()) {
        return data.getJSONObject(0);
      }
    }
    return null;
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("IsOnline")) {
      JSONObject isOnline = json.getJSONObject("IsOnline");
      return "Y".equalsIgnoreCase(isOnline.getString("Sellable"));
    }
    return false;
  }

  @Override
  public String getSku() {
    Element sku = doc.selectFirst("div[data-sku]");
    if (sku != null) {
      return sku.attr("data-sku").split(":")[0];
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element name = doc.selectFirst("meta[property='og:title']");
    if (name != null) {
      return name.attr("content").replaceAll(" \\| Canadian Tire", "");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null) {
      if (json.has("Promo")) {
        return json.getJSONObject("Promo").getBigDecimal("Price");
      }
      if (json.has("Price")) {
        return json.getBigDecimal("Price");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "CanadianTire";
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    Element brand = doc.selectFirst("img.brand-logo-link__img");
    if (brand == null)
      brand = doc.selectFirst("img.brand-footer__logo");

    if (brand != null) {
      return brand.attr("alt");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.pdp-details-features__items li"));
  }

}
