package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Kogan Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Kogan extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

  @Override
  public boolean isAvailable() {
    return (dom.getElementById("form-add-to-cart") != null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemProp='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    
    val = dom.selectFirst("p[itemProp='model']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
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
    
    val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    
    val = dom.selectFirst("h5[itemProp='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemProp='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return "Kogan";
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("a[href$='terms-and-conditions']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Kogan.com";
  }

  @Override
  public String getShipment() {
    Element shippingEL = dom.selectFirst("h5[itemProp='price']");
    if (shippingEL != null) {
    	String text = shippingEL.text();
    	if (text.toLowerCase().contains("free shipping")) {
    		return "FREE SHIPPING";
    	} else {
    		return "SEE THE CONDITIONS";
    	}
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(dom.select("section#specs-accordion dl"), "dt", "dd");
  }

}
