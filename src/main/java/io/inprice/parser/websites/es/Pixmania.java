package io.inprice.parser.websites.es;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * Parser for Pixmania Spain
 *
 * Please note that: link's url (aka main url) is never used for data pulling
 *
 * This is a very special case. Pixmania actually provides nothing in html form.
 * So, we need to make two different request to collect all the data we need. 
 *    a) The first is for token info pulled by product-id, whose steps are explained below 
 *    b) The second is for the data 
 * Besides, in order to make the first request we also need to get product-id,  which a part of the main url
 *
 * Product-id 
 *  - is derived from url by splitting it up by dash - 
 *  - the last part of url is the product-id
 *  - in getJsonData(), the data is pulled with the payload whose steps are explained above 
 *  - all data is built by using bestOffer and json object. the two are set in getJsonData()
 *
 * @author mdpinar
 */
public class Pixmania extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Pixmania.class);
	
	private JSONObject json;
  private JSONObject bestOffer;
	
	@Override
	protected void afterRequest(WebClient webClient) {
    final String productId = findProductId();
    if (productId != null) {

    	String token = null;
      JSONObject tokenData = new JSONObject(getHtml());
      if (tokenData.has("session")) {
      	token = tokenData.getJSONObject("session").getString("token");
      }

      if (StringUtils.isNotBlank(token)) {
    		try {
    			WebRequest req = new WebRequest(new URL("https://www.pixmania.es/api/pcm/products/" + productId), HttpMethod.GET);
    			req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
    			req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
    			req.setAdditionalHeader("Authorization", "Bearer " + token);
    			req.setAdditionalHeader("Language", "es-ES");

    			WebResponse res = webClient.loadWebResponse(req);
    	    if (res.getStatusCode() < 400) {
            JSONObject data = new JSONObject(res.getContentAsString());
            if (data.has("product")) {
            	json = data.getJSONObject("product");
              if (json.has("best_offer")) {
                bestOffer = json.getJSONObject("best_offer");
              }
            }
    	    } else {
          	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
          }
    		} catch (IOException e) {
    			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
    			log.error("Failed to fetch extra data", e);
      	}
      } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (token)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    	}
    } else {
    	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (prod_id)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    }
	}
	

  /**
   * This url is substitute for link's original url Since Pixmania provides
   * nothing in html form
   *
   * @return String - the reference url
   */
  @Override
  protected String getAlternativeUrl() {
    return "https://www.pixmania.es/api/ecrm/session";
  }

  /**
   * Returns product-id which is extracted from main url by splitting it up by
   * dash (the last part we are looking for)
   *
   * @return String - product-id
   */
  private String findProductId() {
    final String[] urlChunks = getUrl().split("\\?");
    if (urlChunks.length > 0) {
      final String[] partChunks = urlChunks[0].split("-");
      if (partChunks.length > 0) {
        return partChunks[partChunks.length - 1];
      }
    }
    return null;
  }

  @Override
  public boolean isAvailable() {
    if (bestOffer != null && bestOffer.has("stock")) {
      return bestOffer.getInt("stock") > 0;
    }
    return false;
  }

  @Override
  public String getSku() {
    if (bestOffer != null && bestOffer.has("sku")) {
      return bestOffer.getString("sku");
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
    if (bestOffer != null && bestOffer.has("price_with_vat")) {
      return bestOffer.getBigDecimal("price_with_vat");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (bestOffer != null && bestOffer.has("merchant")) {
      JSONObject merchant = bestOffer.getJSONObject("merchant");
      if (merchant.has("name")) {
        return merchant.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    if (bestOffer != null && bestOffer.has("free_shipping")) {
      return "Free shipping: " + bestOffer.getBoolean("free_shipping");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      JSONObject merchant = json.getJSONObject("brand");
      if (merchant.has("name")) {
        return merchant.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (json != null && json.has("description")) {
      final String description = json.getString("description");
      final String[] descChunks = description.split("-");
      if (descChunks.length > 0) {
        specList = new ArrayList<>();
        for (String desc : descChunks) {
          specList.add(new LinkSpec("", desc));
        }
      }
    }
    return specList;
  }

}
