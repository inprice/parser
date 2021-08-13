package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * GittiGidiyor, Turkiye
 *
 * Protected by akamai!
 * 
 * https://www.gittigidiyor.com
 *
 * @author mdpinar
 */
public class GittiGidiyorTR extends AbstractWebsite {

	private Document dom;

	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element notFoundDiv = dom.selectFirst(".gg-404-container");
		if (notFoundDiv == null) {
			notFoundDiv = dom.getElementById("sp-passive-product-message");
			if (notFoundDiv == null) {
				return HttpStatus.OK;
			}
		}
		return HttpStatus.NOT_FOUND;
		
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='og:availability']");
    if (val != null) {
    	String content = val.attr("content").toLowerCase();
    	return (content.contains("instock") || content.contains("preorder"));
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.getElementById("productId");
    if (val != null && val.hasAttr("value")) {
      return val.attr("value");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.getElementById("sp-title");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst("span.title");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("productTitle");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element val = dom.getElementById("sp-price");
  	if (val != null && StringUtils.isNotBlank(val.val())) {
  		return new BigDecimal(cleanDigits(val.val()));
  	}
  	
  	val = dom.selectFirst("span.lastPrice");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("sp-price-lowPrice");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    val = dom.selectFirst("[data-price]");
    if (val != null && StringUtils.isNotBlank(val.attr("data-price"))) {
      return new BigDecimal(cleanDigits(val.attr("data-price")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	Element val = dom.getElementById("spp-brand");
  	if (val != null && val.hasAttr("href")) {
  		String[] chunks = val.attr("href").split("/");
  		return chunks[chunks.length-1];
  	}
  	
    val = dom.selectFirst("ul.product-items li a");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst(".mr10.gt-product-brand-0 a");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.getElementById("sp-member-nick");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst(".member-name a strong");
    
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Element val = dom.getElementById("sp-tabContent-shipping-type-text");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst(".CargoInfos");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select("table#sp-productTabFeatures tr"), "td:nth-child(1)", "td:nth-child(2)");

    if (specs == null || specs.size() == 0) {
      specs = getKeyValueSpecs(dom.select("#specs-container ul li"), "span", "strong");
    }

    if (specs == null || specs.size() == 0) {
    	specs = getKeyValueSpecs(dom.select("div.item-container"), "div.item-column:nth-child(1)", "div.item-column:nth-child(2)");
    }
    
    return specs;
  }

}
