package io.inprice.parser.websites.us;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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

	private final String prodUrl = "https://groceries.asda.com/api/items/view?itemid=";
	
	private String html;
	private WebClient webClient;
	
  private JSONObject preLoad;
  private JSONObject product;
  private JSONObject priceData;
  
  @Override
	protected void setHtml(String html) {
  	this.html = html;

    String preData = findAPart(html, "__PRELOADED_STATE__= ", "</script>");
    if (preData != null) {
      preLoad = new JSONObject(preData);
      if (preLoad.has("product")) {
        JSONObject prod = preLoad.getJSONObject("product");
        if (prod.has("productDetails")) {
          JSONObject details = prod.getJSONObject("productDetails");
          if (details.has("item")) {
            product = details.getJSONObject("item");
          }
        }
      }
    }
  }

  @Override
	protected String getHtml() {
		return html;
	}

	@Override
	protected void afterRequest(WebClient webClient) {
		this.webClient = webClient;
	}

  @Override
  public boolean isAvailable() {
    if (product != null) {
      JSONObject available = null;
      if (product.has("available_to_promise_network")) {
        available = product.getJSONObject("available_to_promise_network");
      } else if (product.has("children")) {
        JSONObject children = product.getJSONObject("children");
        if (!children.isEmpty()) {
          Iterator<String> keys = children.keys();
          if (keys.hasNext()) {
            JSONObject firstChild = children.getJSONObject(keys.next());
            available = firstChild.getJSONObject("available_to_promise_network");
          }
        }
      }
      if (available != null) {
        return available.has("availability_status") && "IN_STOCK".equals(available.getString("availability_status"));
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    if (product != null && product.has("productId")) {
      return product.getString("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null && product.has("title")) {
      return product.getString("title");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (product != null && product.has("price")) {
      JSONObject priceEL = product.getJSONObject("price");
      if (priceEL.has("price")) {
        String price = priceEL.getString("price");
        if (StringUtils.isNotBlank(price))
          return new BigDecimal(cleanDigits(price));
      }
    }

    setPriceData();
    if (priceData != null) {
      if (priceData.has("current_retail")) {
        return priceData.getBigDecimal("current_retail");
      }
      if (priceData.has("current_retail_max")) {
        return priceData.getBigDecimal("current_retail_max");
      }
      if (priceData.has("current_retail_min")) {
        return priceData.getBigDecimal("current_retail_min");
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (product != null && product.has("manufacturerBrand")) {
      return product.getString("manufacturerBrand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    if (product != null && product.has("productVendorName")) {
      String seller = product.getString("productVendorName");
      if (!seller.isEmpty())
        return seller;
    }
    return "Target";
  }

  @Override
  public String getShipment() {
    return "Free order pickup";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (product != null && product.has("itemDetails")) {
      JSONObject details = product.getJSONObject("itemDetails");
      if (details.has("bulletDescription")) {
        JSONArray bullets = details.getJSONArray("bulletDescription");
        if (!bullets.isEmpty()) {
          specList = new ArrayList<>();
          for (int i = 0; i < bullets.length(); i++) {
            String spec = bullets.getString(i);
            String[] specChunks = spec.split("</B>");
            String key = "";
            String value;
            if (specChunks.length > 1) {
              key = specChunks[0];
              value = specChunks[1];
            } else {
              value = specChunks[0];
            }
            specList.add(new LinkSpec(key.replaceAll(":|<B>", ""), value));
          }
        }
      }
    }
    return specList;
  }

	private void setPriceData() {
    if (preLoad != null && preLoad.has("config")) {
      JSONObject config = preLoad.getJSONObject("config");
      if (config.has("firefly")) {
        String apiKey = config.getJSONObject("firefly").getString("apiKey");
        if (StringUtils.isNotBlank(apiKey)) {
      		try {
      	    StringBuilder newUrl = new StringBuilder();
      	    newUrl.append(prodUrl);
      	    newUrl.append(getSku());
      	    newUrl.append("?pricing_store_id=1531");
      	    newUrl.append("&key=");
      	    newUrl.append(apiKey);

        		WebRequest req = new WebRequest(new URL(newUrl.toString()), HttpMethod.GET);
        		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
        		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
      
        		WebResponse res = webClient.loadWebResponse(req);
            if (res.getStatusCode() < 400) {
              JSONObject priceEL = new JSONObject(res.getContentAsString());
              if (!priceEL.isEmpty()) {
                priceData = priceEL.getJSONObject("price");
              }
            } else {
            	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
            }
      		} catch (IOException e) {
      			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
      			log.error("Failed to fetch current", e);
      		}
        } else {
        	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (api_key)!" + (getRetry() < 3 ? " RETRYING..." : ""));
        }
      } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (firefly)!" + (getRetry() < 3 ? " RETRYING..." : ""));
      }
    } else {
    	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (preload)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    }
	}

}
