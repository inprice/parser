package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Apple Global
 *
 * Contains standard json (plus remote json) data
 *
 * @author mdpinar
 */
public class Apple extends AbstractWebsite {

	private JSONObject json;
	private boolean isAvailable;
	private String shippingPrice;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

  	String rawJson = findAPart(html, "var configData = ", "};", 1);
  	if (StringUtils.isNotBlank(rawJson)) {

  		rawJson = rawJson.replaceFirst("productCategoriesData", "\"productCategoriesData\"");
  		rawJson = rawJson.replaceFirst("initData", "\"initData\"");
  		rawJson = rawJson.replaceFirst("purchaseInfo", "\"purchaseInfo\"");
    	
    	JSONObject wholeJson =  new JSONObject(rawJson);
    	if (! wholeJson.isEmpty()) {
    		if (wholeJson.has("initData")) {
    			JSONObject initData = wholeJson.getJSONObject("initData");
      		if (initData.has("content")) {
      			JSONObject content = initData.getJSONObject("content");
      			if (content.has("summary")) {
      				json = content.getJSONObject	("summary");
      			}
      		}
    		}
    		if (wholeJson.has("purchaseInfo")) {
    			JSONObject purchaseInfo = wholeJson.getJSONObject("purchaseInfo");
    			isAvailable = (purchaseInfo.has("isBuyable") && purchaseInfo.getBoolean("isBuyable"));
    			if (purchaseInfo.has("shippingPrice")) shippingPrice = Jsoup.parse(purchaseInfo.getString("shippingPrice")).text();
    		}
    	}
    }
		
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("part")) {
      return json.getString("part");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("productName")) {
  		return json.getString("productName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("prices")) {
    	JSONObject prices = json.getJSONObject("prices");
    	if (prices != null) {
    		if (prices.has("total")) {
    			JSONObject total = prices.getJSONObject("total");
    			if (total.has("total")) {
    				return new BigDecimal(cleanDigits(total.getString("total")));
    			}
    		}
    		if (prices.has("seoTotal")) {
    			return new BigDecimal(cleanDigits(prices.getString("seoTotal")));
    		}
    	}
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    return "Apple";
  }

  @Override
  public String getShipment() {
  	if (json != null && json.has("freeShipping")) {
  		if (StringUtils.isNotBlank(json.getString("freeShipping"))) {
  			return json.getString("freeShipping");
  		}
  	}
  	if (StringUtils.isNotBlank(shippingPrice)) return shippingPrice;
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	if (json != null && json.has("options")) {
  		Set<LinkSpec> specs = new HashSet<>();

  		JSONArray options = json.getJSONArray("options");
  		if (options != null && options.length() > 0) {
  			for (int i = 0; i < options.length(); i++) {
  				JSONObject option = options.getJSONObject(i);
  				if (option.has("isNone") && option.getBoolean("isNone") == false) {
  					specs.add(new LinkSpec("", option.getString("metricsLabel")));
  				}
				}
  		}

      return specs;
  	}
  	return null;
  }

}
