package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
  protected void getJsonData() {
  	String rawJson = findAPart(doc.html(), "var configData = ", "};", 1);

  	if (StringUtils.isNotBlank(rawJson)) {
    	//these tags have no surrounding double quotes, lets add!
    	String[] tagsWODblQuotes = { "productCategoriesData", "initData", "purchaseInfo" };
    	for (String part: tagsWODblQuotes) {
  			rawJson = rawJson.replaceAll("("+part+")", "\"$1\"");
  		}
    	
    	JSONObject json =  new JSONObject(rawJson);
    	if (! json.isEmpty()) {
    		if (json.has("initData")) {
    			JSONObject initData = json.getJSONObject("initData");
      		if (initData.has("content")) {
      			JSONObject content = initData.getJSONObject("content");
      			if (content.has("summary")) {
      				json = content.getJSONObject	("summary");
      			}
      		}
    		}
    		if (json.has("purchaseInfo")) {
    			JSONObject purchaseInfo = json.getJSONObject("purchaseInfo");
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
  public String getSeller() {
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
  public String getBrand() {
    return "Apple";
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	if (json != null && json.has("options")) {
  		List<LinkSpec> specs = new ArrayList<>();

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
