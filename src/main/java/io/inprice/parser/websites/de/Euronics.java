package io.inprice.parser.websites.de;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * Parser for Euronics Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Euronics extends AbstractWebsite {

	private static final Logger log = LoggerFactory.getLogger(Euronics.class);

	private Document dom;
	private String name;
	private List<LinkSpec> specList;
	
	private String articelId;
	private String orderNumber;
	private String headquarter;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		
		dom = Jsoup.parse(html);
		name = dom.title();
		specList = getValueOnlySpecList(dom.select(".list--unordered li span"));

		articelId = findAPart(html, "$.detailArticleId = '", "';");
		orderNumber = findAPart(html, "$.articleOrderNumber = '", "';");
		headquarter = findAPart(html, "$.shopContextHeadquarter = '", "';");
	}

	@Override
	protected void afterRequest(WebClient webClient) {
		try {
			StringBuilder payload = new StringBuilder();
			payload.append("detailArticleId=");
			payload.append(articelId);
			payload.append("&articleOrderNumber=");
			payload.append(orderNumber);
			payload.append("&shopContextHeadquarter=");
			payload.append(headquarter);

			WebRequest req = new WebRequest(new URL("https://www.euronics.de/NfDetail/ajax"), HttpMethod.POST);
  		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json, text/javascript, */*; q=0.01");
  		req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
  		req.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			req.setRequestBody(payload.toString());

	    WebResponse res = webClient.loadWebResponse(req);
	    if (res.getStatusCode() < 400) {
	    	String content = res.getContentAsString();
	    	content = content.substring(content.indexOf("<div"), content.lastIndexOf("<script"));
	    	content = content.replaceAll("\\\\\"", "\"");
	    	content = content.replaceAll("\\\\/", "/");
	    	content = content.replaceAll("\\\\n", "");
	    	content = content.replaceAll("\\s{2,}", " ");
	    	
	    	dom = Jsoup.parse(content);
	    } else {
      	setLinkStatus(LinkStatus.NETWORK_ERROR, "ACCESS PROBLEM!" + (getRetry() < 3 ? " RETRYING..." : ""), res.getStatusCode());
      }
		} catch (IOException e) {
			setLinkStatus(LinkStatus.NETWORK_ERROR, e.getMessage(), 400);
			log.error("Failed to post data to Euronics to fetch product info!", e);
		}
	}
	
  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("link[itemprop='availability']");
    if (val != null) {
    	String href = val.attr("href");
    	return href.contains("InStock");
    }
    return false;
  }

  @Override
  public String getSku() {
    return orderNumber;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
  	Element val = dom.selectFirst(".shipping--cost");
  	if (val != null) return val.text();
  	
  	val = dom.selectFirst(".shipping--is-free");
  	if (val != null) return val.text();
  	
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	return specList;
  }

}
