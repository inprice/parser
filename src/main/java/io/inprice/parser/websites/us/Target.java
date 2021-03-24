package io.inprice.parser.websites.us;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
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
 * Parser for Target USA
 *
 * The parsing steps:
 *
 *  - the html body of link's url contains data (in json format) we need 
 *  - in getJsonData(), we get that json data by using substring() method of String class 
 *  - this data is named as product which is hold on a class-level variable
 *  - each data (except for availability and specList) can be gathered using product variable
 *
 * @author mdpinar
 */
public class Target extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Target.class);

  private String apiKey;
  private String tcin;
  private String storeId;

  private JSONObject item;
  private JSONObject desc;
  private JSONObject price;
  
  @Override
	protected void setHtml(String html) {
		super.setHtml(html);

		apiKey = findAPart(html, "\"apiKey\":\"", "\"");
		tcin = findAPart(html, "\"sku\":\"", "\"");
    storeId = findAPart(html, "\"pricing_store_id\":\"", "\"");
  }

	
	@Override
	protected void afterRequest(WebClient webClient) {
		try {
	    StringBuilder prodUrl = new StringBuilder("https://redsky.target.com/redsky_aggregations/v1/web/pdp_client_v1?key=");
	    prodUrl.append(apiKey);
	    prodUrl.append("&tcin=");
	    prodUrl.append(tcin);
	    prodUrl.append("&store_id=none&scheduled_delivery_store_id=none&pricing_store_id=");
	    prodUrl.append(storeId);
			
  		WebRequest req = new WebRequest(new URL(prodUrl.toString()), HttpMethod.GET);
  		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
  		req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
  		req.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
  		req.setAdditionalHeader("referrer", getUrl());
  		req.setAdditionalHeader("credentials", "omit");
  		req.setAdditionalHeader("mode", "cors");

  		WebResponse res = webClient.loadWebResponse(req);
      if (res.getStatusCode() < 400) {
      	JSONObject all = new JSONObject(res.getContentAsString());
      	if (all != null && all.has("data")) {
      		JSONObject data = all.getJSONObject("data");
      		if (data != null && data.has("product")) {
      			JSONObject product = data.getJSONObject("product");
      			if (product != null) {
      				if (product.has("item")) {
      					item = product.getJSONObject("item");
        				if (item.has("product_description")) desc = item.getJSONObject("product_description");
      				}
      				if (product.has("price")) price = product.getJSONObject("price");
      			}
      		}
      	}
      } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
      }
		} catch (IOException e) {
			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
			log.error("Failed to fetch specs", e);
		}
	}

  @Override
  public boolean isAvailable() {
    if (item != null) {
    	if (item.has("cart_add_on_threshold")) {
  			return item.getInt("cart_add_on_threshold") > 0;
  		}
    	if (item.has("eligibility_rules")) {
        JSONObject eligible = item.getJSONObject("eligibility_rules");
        if (eligible != null && eligible.has("hold")) {
          JSONObject hold = eligible.getJSONObject("hold");
          return hold.getBoolean("is_active");
        }
    	}
    }
    return false;
  }

  @Override
  public String getSku() {
    return tcin;
  }

  @Override
  public String getName() {
    if (desc != null && desc.has("title")) {
    	return desc.getString("title");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (price != null && price.has("current_retail")) {
    	return price.getBigDecimal("current_retail");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (item != null && item.has("primary_brand")) {
    	JSONObject brand = item.getJSONObject("primary_brand");
    	if (brand != null && brand.has("name")) {
    		return brand.getString("name");
    	}
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "See shipping conditions";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (desc != null && desc.has("bullet_descriptions")) {
    	JSONArray rows = desc.getJSONArray("bullet_descriptions");
    	if (rows != null && rows.length() > 0) {
    		specList = new ArrayList<>(rows.length());
    		for (int i = 0; i < rows.length(); i++) {
					String row = rows.getString(i);
					String clean = Jsoup.parse(row).text();
					if (clean.indexOf(":") > 0) {
						String[] pair = clean.split(":");
						specList.add(new LinkSpec(pair[0], pair[1]));
					} else {
						specList.add(new LinkSpec("", clean));
					}
				}
    	}
    }

    return specList;
  }

}
