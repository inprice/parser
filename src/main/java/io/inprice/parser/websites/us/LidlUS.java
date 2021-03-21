package io.inprice.parser.websites.us;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
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
 * Parser for Lidl USA
 *
 * Contains standard data, all is extracted from json data gotten from a special
 * url
 *
 * @author mdpinar
 */
public class LidlUS extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(LidlUS.class);
	
  private final String prodUrl = "https://mobileapi.lidl.com/v1/products/";

  private String sku;
  private JSONObject json;

	@Override
	protected void afterRequest(WebClient webClient) {
    final int index = getUrl().indexOf("/products/");
    final String[] urlChunks = getUrl().split("/");

    if (index > 0 && urlChunks.length > 1) {
      sku = urlChunks[urlChunks.length - 1];
      if (StringUtils.isNotBlank(sku)) {
    		try {
      		WebRequest req = new WebRequest(new URL(prodUrl+sku), HttpMethod.GET);
      		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
      		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
    
      		WebResponse res = webClient.loadWebResponse(req);
          if (res.getStatusCode() < 400) {
            json = new JSONObject(res.getContentAsString());
          } else {
          	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
          }
    		} catch (IOException e) {
    			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
    			log.error("Failed to fetch current", e);
    		}
      } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "ID PROBLEM (sku)!" + (getRetry() < 3 ? " RETRYING..." : ""));
      }
    } else {
    	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (url)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    }
	}

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("price")) {
      JSONObject price = json.getJSONObject("price");
      if (price.has("baseQuantity")) {
        return price.getJSONObject("baseQuantity").getInt("value") > 0;
      }
    }
    return true;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("price")) {
      JSONObject price = json.getJSONObject("price");
      if (price.has("currentPrice")) {
        return price.getJSONObject("currentPrice").getBigDecimal("value");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brands")) {
      JSONArray brands = json.getJSONArray("brands");
      if (!brands.isEmpty() && brands.length() > 0) {
        return brands.getString(0);
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (json != null && json.has("longDescription")) {
      String desc = json.getString("longDescription");
      if (StringUtils.isNotBlank(desc)) {
        String features = desc.replaceAll("<ul>|</ul>|<li>", "");
        String[] featureChunks = features.split("</li>");
        if (featureChunks.length > 0) {
          specList = new ArrayList<>();
          for (String val : featureChunks) {
            if (StringUtils.isNotBlank(val) && !val.startsWith("<p>") && !val.startsWith("<div>"))
              specList.add(new LinkSpec("", val));
          }
        }
      }

    }

    return specList;
  }

}
