package io.inprice.parser.websites.au;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
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
 * Parser for AppliancesOnline Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class AppliancesOnline extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(AppliancesOnline.class);
	
	private JSONObject json;
	private JSONObject specsObj;
	/*
	@Override
	protected Renderer getRenderer() {
		return Renderer.HEADLESS;
	}
	*/
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		this.json = new JSONObject(html);
	}

	@Override
	protected void afterRequest(WebClient webClient) {
		String specsUrl = "https://www.appliancesonline.com.au/api/v2/product/specifications/id/" + json.getInt("productId");

		try {
  		WebRequest req = new WebRequest(new URL(specsUrl), HttpMethod.GET);
  		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
  		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");

  		WebResponse res = webClient.loadWebResponse(req);
      if (res.getStatusCode() < 400) {
      	specsObj = new JSONObject(res.getContentAsString());
      } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
      }
		} catch (IOException e) {
			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
			log.error("Failed to fetch specs", e);
		}
	}

  @Override
  protected String getAlternativeUrl() {
    final String indicator = "product/";
    String productName = getUrl().substring(getUrl().indexOf(indicator) + indicator.length());
    return "https://www.appliancesonline.com.au/api/v2/product/slug/" + productName;
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("available")) {
      return json.getBoolean("available");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("productId")) {
      return "" + json.getInt("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("title")) {
      return json.getString("title");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("price")) {
      return json.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("manufacturer")) {
      JSONObject manufacturer = json.getJSONObject("manufacturer");
      if (manufacturer.has("name")) {
        return manufacturer.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Check delivery cost";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    if (specsObj != null && specsObj.has("groupedAttributes")) {
      JSONObject groupedAttributes = specsObj.getJSONObject("groupedAttributes");
      if (!groupedAttributes.isEmpty()) {

        specs = new HashSet<>();
        Iterator<String> keys = groupedAttributes.keys();

        while (keys.hasNext()) {
          String key = keys.next();
          JSONObject attrs = groupedAttributes.getJSONObject(key);
          if (attrs.has("attributes")) {

            JSONArray array = attrs.getJSONArray("attributes");

            if (array.length() > 0) {
              for (int i = 0; i < array.length(); i++) {
                JSONObject attr = array.getJSONObject(i);

                String name = attr.getString("displayName");
                String value = attr.getString("value");
                String type = attr.getString("inputType");

                if ("boolean".equals(type)) {
                  if ("1".equals(value))
                    value = "Yes";
                  else
                    value = "No";
                }

                specs.add(new LinkSpec(name, value.replaceAll("&#9679; ", "& ")));
              }
            }
          }
        }
      }
    }

    return specs;
  }
  
}
