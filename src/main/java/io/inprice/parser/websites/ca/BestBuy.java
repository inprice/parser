package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for BestBuy Canada
 *
 * The page contains json data on which all data is harvested
 *
 * @author mdpinar
 */
public class BestBuy extends AbstractWebsite {

	private JSONObject json;
  private JSONObject product;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

    String rawJson = findAPart(html, "\"product\":", ",\"productSellers\":{");
    if (StringUtils.isNotBlank(rawJson)) {
      json = new JSONObject(rawJson);
      if (json != null && json.has("product")) {
      	product = json.getJSONObject("product");
      }
    }
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
  public String getSku() {
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
  public String getSeller() {
    if (product != null && !product.isNull("seller")) {
      JSONObject seller = product.getJSONObject("seller");
      if (seller != null) {
        return seller.getString("name");
      }
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    return "Sold and shipped by " + getSeller();
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	if (product != null && product.has("specs")) {
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

    return specs;
  }

}
