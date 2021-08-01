package io.inprice.parser.websites.uk;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Set;

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
 * Parser for Asda UK
 *
 * Contains json data placed in html. So, all data is extracted from json
 *
 * @author mdpinar
 */
public class Asda extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Asda.class);

	private final String prodUrl = "https://groceries.asda.com/api/items/view?itemid=";
	
  private JSONObject json;

	@Override
	protected void afterRequest(WebClient webClient) {
    String[] urlChunks = getUrl().split("/");

    if (urlChunks.length > 1) {
      String productId = urlChunks[urlChunks.length - 1];
      if (productId.matches("\\d+")) {
    		try {
      		WebRequest req = new WebRequest(new URL(prodUrl+productId), HttpMethod.GET);
      		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
      		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
    
      		WebResponse res = webClient.loadWebResponse(req);
          if (res.getStatusCode() < 400) {
            JSONObject prod = new JSONObject(res.getContentAsString());
            if (prod.has("items")) {
              JSONArray items = prod.getJSONArray("items");
              if (items.length() > 0) {
                json = items.getJSONObject(0);
              }
            }
          } else {
          	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
          }
    		} catch (IOException e) {
    			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
    			log.error("Failed to fetch current", e);
    		}
      } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "ID MATCHING PROBLEM (prod_id)!" + (getRetry() < 3 ? " RETRYING..." : ""));
      }
    } else {
    	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (prod_id)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    }
	}

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("availability")) {
      return "A".equals(json.getString("availability"));
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("id")) {
      return json.getString("id");
    }
    return Consts.Words.NOT_AVAILABLE;
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
      return new BigDecimal(cleanDigits(json.getString("price")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brandName")) {
      return json.getString("brandName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "In-store pickup.";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return null;
  }

}
