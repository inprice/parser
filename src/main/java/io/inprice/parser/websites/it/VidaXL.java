package io.inprice.parser.websites.it;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import io.inprice.parser.websites.es.Pixmania;

/**
 * Parser for vidaXL Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class VidaXL extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Pixmania.class);

	private final String prodUrl = "https://www.vidaxl.it/platform/index.php?m=auction&a=getAuctionsList&id=";
	
	private Document dom;
	private String brand;
  private JSONObject prod;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);
		brand = findAPart(html, "\"brand\":\"", "\"");
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

	@Override
	protected void afterRequest(WebClient webClient) {
    Element auctionId = dom.getElementById("auctionId");
    if (auctionId != null && StringUtils.isNotBlank(auctionId.val())) {
  		try {
    		WebRequest req = new WebRequest(new URL(prodUrl+auctionId.val()), HttpMethod.GET);
    		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json");
    		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/json");
  
    		WebResponse res = webClient.loadWebResponse(req);
        if (res.getStatusCode() < 400) {
          JSONObject auction = new JSONObject(res.getContentAsString());
          if (auction.has("current")) {
            prod = auction.getJSONObject("current");
          }
        } else {
        	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
        }
  		} catch (IOException e) {
  			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
  			log.error("Failed to fetch current", e);
  		}
    } else {
    	setLinkStatus(LinkStatus.NETWORK_ERROR, "DATA PROBLEM (auction_id)!" + (getRetry() < 3 ? " RETRYING..." : ""));
    }
	}

  @Override
  public boolean isAvailable() {
    Element inStock = dom.selectFirst("div#not-available div.false");
    return (inStock != null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = dom.selectFirst("input[name='hidden_sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return val.attr("value");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (prod != null && prod.has("price")) {
      return new BigDecimal(cleanDigits(prod.getString("price")));
    }

    Element val = dom.selectFirst("meta[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (brand != null) {
      return brand;
    }

    return "VidaXL";
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("meta[itemprop='seller']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return "VidaXL";
  }

  @Override
  public String getShipment() {
    StringBuilder sb = new StringBuilder();

    Element val = dom.selectFirst("div.delivery-name");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst("div.delivery-info");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text());
      sb.append(". ");
    }

    val = dom.selectFirst("div.shipping-from");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text());
      sb.append(". ");
    }

    val = dom.selectFirst("div.delivery-seller");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text());
    }

    if (sb.length() == 0) sb.append(Consts.Words.NOT_AVAILABLE);

    return sb.toString().replaceAll(" Disponibile Non disponibile", "");
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("ul.specs li"));
  }

}
