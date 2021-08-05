package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for CanadianTire Canada
 *
 * We need to make one more request since some data pieces do not come with the first call!
 *
 * @author mdpinar
 */
public class CanadianTire extends AbstractWebsite {

	private Document dom;
  private JSONObject json;
	
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().contains("404 |") == false) {
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

	@Override
	protected String getExtraUrl() {
    StringBuilder offerUrl = new StringBuilder("view-source:https://api-triangle.canadiantire.ca/esb/PriceAvailability?SKU=");
    offerUrl.append(getSku());
    offerUrl.append("&");
    offerUrl.append("Store=0144");
    offerUrl.append("&");
    offerUrl.append("Banner=CTR");
    offerUrl.append("&");
    offerUrl.append("isKiosk=FALSE");
    offerUrl.append("&");
    offerUrl.append("Language=E");
    offerUrl.append("&=");
    offerUrl.append("_");
    offerUrl.append(new Date().getTime());
		return offerUrl.toString();
	}

  @Override
	protected HttpStatus setExtraHtml(String html) {
		Document dom = Jsoup.parse(html);
		Element jsonEl = dom.selectFirst("pre");

		if (jsonEl != null && StringUtils.isNotBlank(jsonEl.text())) {
			JSONArray rawArr = new JSONArray(jsonEl.text());
			json = rawArr.getJSONObject(0);
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
  }

  @Override
  public boolean isAvailable() {
  	if (json != null && json.has("IsOnline")) {
  		JSONObject isOnline = json.getJSONObject("IsOnline");
  		if (! isOnline.isEmpty()) {
  			return isOnline.getString("Sellable").equals("Y");
  		}
  	}

  	Elements quantities = dom.select("span.instock-quantity");
  	return (quantities != null && quantities.size() > 1);
  }

  @Override
  public String getSku() {
  	String url = getUrl();
		int pPoint = url.indexOf("p.");
		return url.substring(pPoint-7, pPoint);
  }

  @Override
  public String getName() {
    Element name = dom.selectFirst(".js-product-name");
    if (name != null) {
      return name.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	if (json != null) {
  		if (json.has("Promo")) {
    		JSONObject promo = json.getJSONObject("Promo");
    		if (! promo.isEmpty()) {
    			return promo.getBigDecimal("Price");
    		}
  		}
  		if (json.has("Price")) {
  			return json.getBigDecimal("Price");
  		}
  	}

  	Element price = dom.selectFirst("span.price__reg-value");
    if (price != null) {
      return new BigDecimal(cleanDigits(price.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element brand = dom.selectFirst("img.brand-logo-link__img");
    if (brand == null) brand = dom.selectFirst("img.brand-footer__logo");

    if (brand != null) {
      return brand.attr("alt");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select("div.pdp-details-features__items li"));
  }

}
