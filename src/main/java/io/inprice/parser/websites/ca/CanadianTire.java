package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
	
	private JSONObject json;
	
	@Override
	protected void renderExtra(WebDriver webDriver) {
		String name = getName();
		BigDecimal price = BigDecimal.ZERO;
		
  	Element priceEL = doc.selectFirst("span.price__reg-value");
    if (priceEL != null) {
    	price = new BigDecimal(cleanDigits(priceEL.text()));
    }
		
		if (StringUtils.isNotBlank(name) && ! Consts.Words.NOT_AVAILABLE.equals(name) && (price.compareTo(BigDecimal.ZERO) <= 0)) {
  		try {
  	    StringBuilder payload = new StringBuilder("https://api-triangle.canadiantire.ca/esb/PriceAvailability?SKU=");
  	    payload.append(getSku());
  	    payload.append("&");
  	    payload.append("Store=0144");
  	    payload.append("&");
  	    payload.append("Banner=CTR");
  	    payload.append("&");
  	    payload.append("isKiosk=FALSE");
  	    payload.append("&");
  	    payload.append("Language=E");
  	    payload.append("&=");
  	    payload.append("_");
  	    payload.append(new Date().getTime());
  			webDriver.navigate().to(payload.toString());
  			
  			String rawJson = webDriver.findElement(By.tagName("pre")).getText();
				JSONArray arr = new JSONArray(rawJson);
				json = arr.getJSONObject(0);

    	} catch (Exception e) {
    		log.error("Failed to do extra: {}", e.getMessage());
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

  	Elements quantities = doc.select("span.instock-quantity");
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
    Element name = doc.selectFirst(".js-product-name");
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

  	Element price = doc.selectFirst("span.price__reg-value");
    if (price != null) {
      return new BigDecimal(cleanDigits(price.text()));
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
    if (brand == null) brand = doc.selectFirst("img.brand-footer__logo");

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
