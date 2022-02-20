package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Walmart, Canada
 * 
 * @author mdpinar
 */
public class WalmartXX extends AbstractWebsite {

	private static final Logger logger = LoggerFactory.getLogger(WalmartXX.class);

	private JSONObject json;
	private JSONObject product;
	private JSONObject context;
	
	@Override
	public ParseStatus startParsing(Link link, String html) {
		Document dom = Jsoup.parse(html);

    Element jsonEL = dom.getElementById("__NEXT_DATA__");
    if (jsonEL != null) {
    	JSONObject allData = new JSONObject(jsonEL.childNodes().get(0).toString());
    	if (allData != null && allData.has("props")) {
    		try {
    			json = getJSONObject(allData, "props.pageProps.initialData.data");
    			context = getJSONObject(json, "contentLayout.pageMetadata.pageContext.itemContext");
    			product = json.getJSONObject("product");
    			
    			return OK_Status();
    		} catch (Exception e) {
    			logger.error("Failed to parse json text", e);
    		}
    	}
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	if (product != null) {
  		return product.getString("availabilityStatus").equals("IN_STOCK");
  	}
    return false;
  }

  @Override
  public String getSku() {
  	if (context != null) {
  		return context.getString("itemId");
  	}
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	if (json != null) {
  		return context.getString("name");
  	}
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	if (product != null) {
  		return product.getJSONObject("priceInfo").getJSONObject("currentPrice").getBigDecimal("price");
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (json != null) {
  		return context.getString("brand");
  	}
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
  	if (product != null) {
  		return product.getString("sellerName");
  	}
  	return super.getSeller();
  }

  @Override
  public String getShipment() {
  	if (product != null) {
  		return product.getJSONArray("fulfillmentLabel").getJSONObject(0).getString("shippingText");
  	}
    return "Shipped by Walmart Canada";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;
  	
  	if (json != null && json.has("idml")) {
	  	JSONObject idml = json.getJSONObject("idml");
	
	    if (idml.has("specifications")) {
	      JSONArray specsArr = idml.getJSONArray("specifications");
	      if (specsArr.length() > 0) {
	        specs = new HashSet<>();
	        for (int i=0; i < specsArr.length(); i++) {
	        	JSONObject specPair = specsArr.getJSONObject(i);
            specs.add(new LinkSpec(specPair.getString("name"), specPair.getString("value")));
	        }
	      }
	    }
  	}
    return specs;
  }

}
