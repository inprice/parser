package io.inprice.parser.websites.us;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

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

  private final String prodUrl = "https://mobileapi.lidl.com/v1/products/";

  private String sku;
  private JSONObject json;

	@Override
	protected WebResponse makeRequest(WebClient webClient) throws MalformedURLException, IOException {
    final String[] urlChunks = getUrl().split("/");
    sku = urlChunks[urlChunks.length - 1];

		WebRequest req = new WebRequest(new URL(prodUrl+sku+"?storeId=US01053"), HttpMethod.GET);
		req.setAdditionalHeader(HttpHeader.ACCEPT, "*/*");
		req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
		req.setAdditionalHeader("referrer", getUrl());
		req.setAdditionalHeader("credentials", "omit");
		req.setAdditionalHeader("mode", "cors");
		
		return webClient.loadWebResponse(req);
	}
	
	@Override
	protected void setHtml(String html) {
		json = new JSONObject(html);
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
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    if (json != null && json.has("longDescription")) {
      String desc = json.getString("longDescription");
      if (StringUtils.isNotBlank(desc)) {
        String features = desc.replaceAll("<ul>|</ul>|<li>", "");
        String[] featureChunks = features.split("</li>");
        if (featureChunks.length > 0) {
          specs = new HashSet<>();
          for (String val : featureChunks) {
            if (StringUtils.isNotBlank(val) && !val.startsWith("<p>") && !val.startsWith("<div>"))
              specs.add(new LinkSpec("", val));
          }
        }
      }

    }

    return specs;
  }

}
