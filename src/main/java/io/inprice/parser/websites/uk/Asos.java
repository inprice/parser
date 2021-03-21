package io.inprice.parser.websites.uk;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
 * Parser for Asos UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Asos extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Asos.class);

	//WARN: country selection may cause problems! it should have been more elegant but how!
	private static final String BROWSE_COUNTRY = "browseCountry=JP";
	
	private Document dom;
  private JSONObject offer;

  private String priceUrl;
  private BigDecimal price = BigDecimal.ZERO;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    String prodData = findAPart(html, "config.product = ", "};", 1);
    if (StringUtils.isNotBlank(prodData)) {
      offer = new JSONObject(prodData);
    }
    priceUrl = "https://www.asos.com" + findAPart(html, "config.stockPriceApiUrl = '", "';");
	}
	
	@Override
	protected void beforeRequest(WebRequest req) {
		req.setAdditionalHeader(HttpHeader.COOKIE, BROWSE_COUNTRY);
	}
	
	@Override
	protected void afterRequest(WebClient webClient) {
		try {
  		WebRequest req = new WebRequest(new URL(priceUrl), HttpMethod.GET);
  		req.setAdditionalHeader(HttpHeader.ACCEPT, "*/*");
  		req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
  		req.setAdditionalHeader(HttpHeader.COOKIE, BROWSE_COUNTRY);

  		WebResponse res = webClient.loadWebResponse(req);
      if (res.getStatusCode() < 400) {
      	JSONArray prices = new JSONArray(res.getContentAsString());
      	if (prices != null && prices.length() > 0) {
        	for (int i = 0; i < prices.length(); i++) {
  					JSONObject prod = prices.getJSONObject(i);
  					if (prod != null && prod.has("productId")) {
  						if (offer.getInt("id") ==  prod.getInt("productId")) {
  							JSONObject pprice = prod.getJSONObject("productPrice");
  							if (pprice != null && pprice.has("current")) {
  								JSONObject current = pprice.getJSONObject("current");
  								if (current != null && current.has("value")) {
  									price = current.getBigDecimal("value");
  								}
  							}
  							break;
  						}
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
  	if (offer != null && offer.has("isInStock")) {
  		return offer.getBoolean("isInStock");
  	}
    return false;
  }

  @Override
  public String getSku() {
  	if (offer != null && offer.has("id")) {
  		return ""+offer.getInt("id");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	if (offer != null && offer.has("name")) {
  		return offer.getString("name");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public String getBrand() {
  	if (offer != null && offer.has("brandName")) {
  		return offer.getString("brandName");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("#shipping-restrictions .shipping-restrictions");
    if (val != null && StringUtils.isNotBlank(val.attr("style"))) {
      String style = val.attr("style");
      if (style != null)
        return "Please refer to Delivery and returns info section";
    }

    val = dom.getElementById("shippingRestrictionsLink");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    } else {
      return "See delivery and returns info";
    }
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("div.product-description li"));
  }

}
