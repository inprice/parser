package io.inprice.parser.websites.ca;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for CanadianTire Canada
 *
 * Please note that html can be pulled completely after js render!
 *
 * @author mdpinar
 */
public class CanadianTire extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(CanadianTire.class);
	
	private Document dom;
  private JSONObject json;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
	}

	@Override
	protected void afterRequest(WebClient webClient) {
		String name = getName();
		BigDecimal price = BigDecimal.ZERO;
		
  	Element priceEL = dom.selectFirst("span.price__reg-value");
    if (priceEL != null) {
    	price = new BigDecimal(cleanDigits(priceEL.text()));
    }
		
		if (StringUtils.isNotBlank(name) && ! Consts.Words.NOT_AVAILABLE.equals(name) && (price.compareTo(BigDecimal.ZERO) <= 0)) {
  		try {
  	    StringBuilder offerUrl = new StringBuilder("https://api-triangle.canadiantire.ca/esb/PriceAvailability?SKU=");
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

  			WebRequest req = new WebRequest(new URL(offerUrl.toString()), HttpMethod.GET);
  			req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
  			req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");

  			WebResponse res = webClient.loadWebResponse(req);
  	    if (res.getStatusCode() < 400) {
  	    	Document subdom = Jsoup.parse(res.getContentAsString());
  	    	Element pre = subdom.selectFirst("pre");
  	    	if (pre == null) pre = subdom.body();

  				JSONArray arr = new JSONArray(pre.text());
  				json = arr.getJSONObject(0);
  	    } else {
        	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
        }
  		} catch (IOException e) {
  			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
  			log.error("Failed to fetch extra data", e);
    	}
		}
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
  public String getSeller() {
    return "CanadianTire";
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("div.pdp-details-features__items li"));
  }

}
