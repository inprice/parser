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

	private final String prodUrl = "https://www.asos.com/api/product/catalogue/v3/stockprice?store=ROW&productIds=";

	private Document dom;

  private BigDecimal price = BigDecimal.ZERO;
  private boolean isAvailable;
  private String sku;
  private String name;
  private String brandName;
  private String shipment;
  private JSONObject offer;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    String prodData = findAPart(html, "window.asos.pdp.config.product =", "};", 1);
    if (StringUtils.isNotBlank(prodData)) {
      offer = new JSONObject(prodData);
    }

    if (offer != null) {
      sku = "" + offer.getInt("id");
      isAvailable = offer.getBoolean("isInStock");
      name = offer.getString("name");
      brandName = offer.getString("brandName");
      JSONObject shipping = offer.getJSONObject("shippingRestrictions");
      if (shipping != null && shipping.has("shippingRestrictionsLabel")) {
        shipment = shipping.getString("shippingRestrictionsLabel");
      }
    } else {
      sku = getSku();
    }
	}

	@Override
	protected void afterRequest(WebClient webClient) {
    if (StringUtils.isNotBlank(sku)) {
  		try {
    		WebRequest req = new WebRequest(new URL(prodUrl+sku), HttpMethod.GET);
    		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
    		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
  
    		boolean hasDataProblem = true;

    		WebResponse res = webClient.loadWebResponse(req);
        if (res.getStatusCode() < 400) {
          JSONArray items = new JSONArray(res.getContentAsString());
          if (items.length() > 0) {
            JSONObject parent = items.getJSONObject(0);

            if (parent.has("productPrice")) {
              JSONObject pprice = parent.getJSONObject("productPrice");
              price = pprice.getJSONObject("current").getBigDecimal("value");
            }

            if (!isAvailable && parent.has("variants")) {
              JSONArray variants = parent.getJSONArray("variants");
              if (variants.length() > 0) {
              	hasDataProblem = false;
                for (int i = 0; i < variants.length(); i++) {
                  JSONObject var = variants.getJSONObject(i);
                  if (var.getBoolean("isInStock")) {
                    isAvailable = true;
                    break;
                  }
                }
              }
            }
          }
        	
        } else {
        	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
        }
      
        if (hasDataProblem) {
        	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""));
        }
        
  		} catch (IOException e) {
  			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
  			log.error("Failed to fetch current", e);
  		}
    } else {
    	setLinkStatus(LinkStatus.NETWORK_ERROR, "ID PROBLEM (sku)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    }
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    if (StringUtils.isNotBlank(sku)) return sku;

    Element val = dom.selectFirst("span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    String[] urlChunks = getUrl().split("/");
    if (urlChunks.length > 1) {
      String prdId = urlChunks[urlChunks.length - 1];
      return prdId.substring(0, prdId.indexOf("?"));
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (StringUtils.isNotBlank(name)) return name;

    Element name = dom.selectFirst("div.product-hero h1");
    if (name != null) {
      return name.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public String getBrand() {
    if (StringUtils.isNotBlank(brandName)) return brandName;

    Element val = dom.selectFirst("span[itemprop='brand'] span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("span[itemprop='seller'] span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    if (StringUtils.isNotBlank(shipment)) return shipment;

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
