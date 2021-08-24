package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.HashSet;
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
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * BestBuy, Canada
 *
 * https://www.bestbuy.ca
 *
 * @author mdpinar
 */
public class BestBuyCA extends AbstractWebsite {

	private JSONObject json;
  private JSONObject product;
	
	@Override
	protected ParseStatus setHtml(String html) {
		Document dom = Jsoup.parse(html);

		String title = dom.selectFirst("title").text().toLowerCase();
		if (title.contains("site down") == true) {
			return new ParseStatus(503, "Site is down!");
		} else if (title.contains("not found") == false) {
      String rawJson = findAPart(html, "}},\"product\":", ",\"productSellers\":");
      if (StringUtils.isNotBlank(rawJson)) {
        json = new JSONObject(rawJson);
        if (json != null && json.has("product")) {
        	product = json.getJSONObject("product");
        	return ParseStatus.PS_OK;
        }
      }
		} 
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	boolean isAvailable = false;

    if (json != null && json.has("availability")) {
    	JSONObject availability = json.getJSONObject("availability");
    	if (availability != null) {
    		if (availability.has("shipping")) {
    			JSONObject shipping = availability.getJSONObject("shipping");
    			if (shipping != null && shipping.has("purchasable")) {
    				isAvailable = shipping.getBoolean("purchasable");
    			}
    		}
    		if (isAvailable == false && availability.has("pickup")) {
    			JSONObject pickup = availability.getJSONObject("pickup");
    			if (pickup != null && pickup.has("purchasable")) {
    				isAvailable = pickup.getBoolean("purchasable");
    			}
    		}
    	}
    }

    return isAvailable;
  }

  @Override
  public String getSku(String url) {
  	if (json != null && json.has("availability")) {
    	JSONObject availability = json.getJSONObject("availability");
    	if (availability != null && availability.has("sku")) {
    		return availability.getString("sku");
    	}
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null && product.has("name")) {
      return product.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (product != null && product.has("priceWithEhf")) {
      return product.getBigDecimal("priceWithEhf");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (product != null && product.has("brandName")) {
      return product.getString("brandName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller(String defaultSeller) {
    if (product != null && !product.isNull("seller")) {
      JSONObject seller = product.getJSONObject("seller");
      if (seller != null) {
        return seller.getString("name");
      }
    }
    return defaultSeller;
  }

  @Override
  public String getShipment() {
    return "Sold and shipped by BestBuy";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	if (product != null && product.has("specs")) {
  		if (product.has("specs")) {
    		JSONObject specsObj = product.getJSONObject("specs");
    		if (specsObj != null && specsObj.keySet().size() > 0) {
    			specs = new HashSet<>(specsObj.keySet().size());
  
    			for (String cat: specsObj.keySet()) {
    				JSONArray specCatRows = specsObj.getJSONArray(cat);
    				for (int i = 0; i < specCatRows.length(); i++) {
    					JSONObject spec = specCatRows.getJSONObject(i);
    					specs.add(new LinkSpec(cat + ": " + spec.getString("name"), spec.getString("value")));
  					}
  				}
    			
    		}
  		}
  		if (product.has("longDescription")) {
  			Document specDom = Jsoup.parse(product.getString("longDescription"));
  			Elements features = specDom.getElementsByTag("p");
  			if (features.size() > 0) {
  				if (specs == null) specs = new HashSet<>(features.size());
  				for (Element spec: features) {
  					specs.add(new LinkSpec("", spec.text().replaceAll(" - ", "")));
  				};
  			}
  		}
  	}

    return specs;
  }

}
