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

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * CanadianTire, Canada
 *
 * https://www.canadiantire.ca
 *
 * @author mdpinar
 */
public class CanadianTireCA extends AbstractWebsite {

	private Document dom;
  private JSONObject json;

  private String url;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().contains("404 |") == false) {
		  url = link.getUrl();
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

	/**
	 * waitBy() method has no effect on this website, so an extra call needed!
	 */
	@Override
	protected String getExtraUrl(String url) {
    StringBuilder offerUrl = new StringBuilder("view-source:https://api-triangle.canadiantire.ca/esb/PriceAvailability?SKU=");
    offerUrl.append(url);
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
	protected ParseStatus setExtraHtml(String html) {
		Document dom = Jsoup.parse(html);
		Element jsonEl = dom.selectFirst("pre");

		if (jsonEl != null && StringUtils.isNotBlank(jsonEl.text())) {
			JSONArray rawArr = new JSONArray(jsonEl.text());
			json = rawArr.getJSONObject(0);
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
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
