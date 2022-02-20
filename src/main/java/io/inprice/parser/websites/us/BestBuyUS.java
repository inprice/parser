package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.StringHelper;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * BestBuy, USA
 * 
 * https://www.bestbuy.com
 *
 * @author mdpinar
 */
public class BestBuyUS extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

  private final String URL_POSTFIX = "intl=nosplash";
  
  @Override
  protected String getUrl(String url) {
  	if (url.indexOf(URL_POSTFIX) < 0) {
			return url + (url.indexOf('?') > 0 ? "&" : "?") + URL_POSTFIX;
  	} else {
  		return url;
  	}
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		
    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (CollectionUtils.isNotEmpty(dataEL)) {
    	for (DataNode dNode : dataEL.dataNodes()) {
    		try {
	        JSONObject data = new JSONObject(StringHelper.escapeJSON(dNode.getWholeData()));
	        if (data.has("@type")) {
	          String type = data.getString("@type");
	          if (type.equals("Product")) {
	          	json = data;
	            if (json.has("offers")) {
	            	Object offersObj = json.get("offers");
	            	if (offersObj instanceof JSONObject) {
	            		offers = json.getJSONObject("offers");
	            	} else {
	            		JSONArray offersArr = (JSONArray) offersObj;
	            		offers = offersArr.getJSONObject(0);
	            	}
	          		return OK_Status();
	            }
		        }
	        }
    		} catch (Exception e) { }
      }
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("offers")) {
    	JSONArray subOffers = offers.getJSONArray("offers");
    	if (subOffers != null && subOffers.length() > 0) {
        String availability = subOffers.getJSONObject(0).getString("availability").toLowerCase();
        return availability.contains("instock") || availability.contains("preorder");
    	}
    }
    if (offers != null && offers.has("offercount")) {
    	return offers.getInt("offercount") > 0;
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (offers != null && offers.has("offers")) {
    	JSONArray subOffers = offers.getJSONArray("offers");
    	if (subOffers != null && subOffers.length() > 0) {
    		return new BigDecimal(cleanDigits(subOffers.getJSONObject(0).getString("price")));
    	}
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
    	return json.getJSONObject("brand").getString("name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return null; //getKeyValueSpecs(dom.select(".specs-table ul"), "li .row-title", "li .row-value");
  }

}
