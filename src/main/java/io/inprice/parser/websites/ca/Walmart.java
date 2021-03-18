package io.inprice.parser.websites.ca;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Walmart CA!
 *
 * @author mdpinar
 */
public class Walmart extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Walmart.class);

	private Document dom;

	private String sku;
	private String name;
	private String brand;
	private JSONObject json;
	private String features;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

		dom = Jsoup.parse(html);
		features = findAPart(html, "featuresSpecifications\":\"", "\",\"type\"");
	}

	@Override
	protected void afterRequest(WebClient webClient) {
    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (dataEL != null) {
      for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
        if (data.has("@type")) {
          String type = data.getString("@type");
          if (type.equals("Product")) {
          	sku = data.getString("sku");
          	name = data.getString("name");
            JSONObject branding = data.getJSONObject("brand");
            if (branding != null && branding.has("name")) {
            	brand = branding.getString("name");
            }
            break;
          }
        }
      }
    }
    
    if (StringUtils.isBlank(sku)) {
			log.warn("Missing info! SKU: {}, Name: {}, Brand: {}", sku, name, brand);
    	return;
    }
		
    String html = dom.html();
    
    String ind = "\"item\":{\"id\":\"";
    int pos = html.indexOf(ind)+ind.length();
    String productId = html.substring(pos, html.indexOf("\"", pos));
    
    ind = "{\"storeId\":\"";
    pos = html.indexOf(ind)+ind.length();
    String storeId = html.substring(pos, html.indexOf("\"", pos));
    
    ind = "{\"experience\":\"";
    pos = html.indexOf(ind)+ind.length();
    String experience = html.substring(pos, html.indexOf("\"", pos));
    
    ind = "\"{\\\"postalCode\\\":\\\"";
    pos = html.indexOf(ind)+ind.length();
    String postalCode = html.substring(pos, html.indexOf("\"", pos)).substring(0, 3);

		try {
			StringBuilder payload = new StringBuilder();
			payload.append("{\"fsa\":\"");
			payload.append(postalCode);
			payload.append("\",\"products\":[{\"productId\":\"");
			payload.append(productId);
			payload.append("\",\"skuIds\":[\"");
			payload.append(sku);
			payload.append("\"]}],\"lang\":\"en\",\"pricingStoreId\":\"");
			payload.append(storeId);
			payload.append("\",\"fulfillmentStoreId\":\"");
			payload.append(storeId);
			payload.append("\",\"experience\":\"");
			payload.append(experience);
			payload.append("\"}");

			WebRequest req = new WebRequest(new URL("https://www.walmart.ca/api/product-page/v2/price-offer"), HttpMethod.POST);
  		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
  		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
			req.setRequestBody(payload.toString());

	    WebResponse res = webClient.loadWebResponse(req);
	    if (res.getStatusCode() < 400) {
  	    JSONObject data = new JSONObject(res.getContentAsString());
  	    if (data != null && data.has("offers")) {
  	    	JSONObject offers = data.getJSONObject("offers");
  	    	if (! offers.isEmpty()) {
    	    	String key = offers.keys().next();
    	    	json = offers.getJSONObject(key);
  	    	}
  	    }
	    } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
      }
		} catch (IOException e) {
			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
			log.error("Failed to post data to Walmart to fetch product price!", e);
		}
		
		if (json == null || json.isEmpty()) {
			log.warn("Missing json info!");
		}
	}

  @Override
  public boolean isAvailable() {
  	if (json != null && json.has("gmAvailability")) {
  		return json.getString("gmAvailability").equals("Available");
  	}
    return false;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public BigDecimal getPrice() {
  	if (json != null && json.has("currentPrice")) {
  		return json.getBigDecimal("currentPrice");
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    return brand;
  }

  @Override
  public String getSeller() {
  	if (json != null && json.has("sellerInfo")) {
  		JSONObject sellerInfo = json.getJSONObject("sellerInfo");
  		return sellerInfo.getString("en");
  	}
    return "Walmart";
  }

  @Override
  public String getShipment() {
  	Element shipment = dom.selectFirst("div[data-automation='fulfillment-options-shipping']");
  	if (shipment != null) {
      return shipment.text();
    }

  	shipment = dom.selectFirst("div[data-automation='fulfillment-options-pickup']");
  	if (shipment != null) {
      return shipment.text();
    }

    return "Shipped by " + getSeller();
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (features != null) {
      String[] specs = features.split("•");
      if (specs.length > 0) {
        specList = new ArrayList<>();
        for (String spec : specs) {
          if (StringUtils.isNotBlank(spec)) {
            final String clean = spec.replaceAll(".u2028|.u003C", "").replaceAll("br>", "");
            specList.add(new LinkSpec("", clean));
          }
        }
      }
    }
    return specList;
  }

  protected void detectProblem() {
  	String title = dom.title();
		if (title.contains("Verify Your Identity")) {
			setLinkStatus(LinkStatus.BLOCKED, "BLOCKED!" + (getRetry() < 3 ? " RETRYING..." : ""));
			return;
		}

  	Elements h1s = dom.select("h1");
  	if (h1s != null && h1s.size() > 0) {
  		for (Element h1: h1s) {
				if (h1.text().contains("Clean up in Aisle 404")) {
					setLinkStatus(LinkStatus.NOT_FOUND, "NOT FOUND!");
					return;
				}
			}
  	}
  	super.detectProblem();
  }

}
