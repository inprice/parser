package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebRequest;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Best Buy USA!
 *
 * @author mdpinar
 */
public class BestBuyUS extends AbstractWebsite {

	private Document dom;
	private String referer;

	@Override
	protected String getAlternativeUrl() {
		String url = getUrl();
		boolean hasIntl = url.indexOf("intl=nosplash") > 0;
		
		if (hasIntl) {
			referer = url.replaceAll(".intl=nosplash", "");
		} else {
			referer = url;
			url = url + (url.indexOf("?") > 0 ? "&" : "?") + "intl=nosplash";
		}
		return url;
	}
	
	@Override
	protected void beforeRequest(WebRequest req) {
		req.setAdditionalHeader(HttpHeader.REFERER, referer);
	}
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);
	}

  @Override
  public boolean isAvailable() {
    return (dom.selectFirst(".inactive-product-message") == null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst(".sku .product-data-value");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst(".sku-title h1");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst(".priceView-hero-price .sr-only");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("a.btn-brand-link");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
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
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select(".specs-table ul"), "li .row-title", "li .row-value");
  }

}
