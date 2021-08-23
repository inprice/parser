package io.inprice.parser.websites.fr;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * CDiscount, France
 *
 * https://www.cdiscount.com
 *
 * @author mdpinar
 */
public class CDiscountFR extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected By waitBy() {
		return By.id("fpSku");
	}

	@Override
	protected ParseStatus setHtml(String html) {
		dom = Jsoup.parse(html);
		
		Element notFoundImg = dom.selectFirst("img[alt='404']");
		if (notFoundImg == null) {
			return ParseStatus.PS_OK;
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	Element val = dom.getElementById("fpAddBsk");
  	return (val != null && val.hasAttr("disabled") == false);
  }

  @Override
  public String getSku(String url) {
  	Element val = dom.getElementById("fpSku");
  	if (val != null) {
  		return val.text();
  	}
  	return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
  		return val.attr("content");
  	}
  	return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element val = dom.selectFirst("[itemprop='price']");
  	if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	Element val = dom.selectFirst("[itemprop='brand']");
  	if (val != null) {
  		return val.text();
  	}
  	return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller(String defaultSeller) {
  	Element val = dom.selectFirst("fpSellerName");
  	if (val != null) {
  		return val.text();
  	}
  	return defaultSeller;
  }

  @Override
  public String getShipment() {
  	Element val = dom.getElementById("fpShippingMessage");
  	if (val != null) {
  		return val.text();
  	}
    return "Check shipping conditions";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("#descContent tr"), "td:nth-child(1)", "td:nth-child(2)");
  }

}
