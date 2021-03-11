package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Best Buy USA!
 *
 * @author mdpinar
 */
public class BestBuy extends AbstractWebsite {
	
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
	protected Map<String, String> getExtraHeaders() {
		Map<String, String> headers = new HashMap<>(3);
		headers.put("Referer", referer);
		headers.put("DNT", "1");
		headers.put("Upgrade-Insecure-Requests", "1");
		return headers;
	}
	
  @Override
  public boolean isAvailable() {
    return (doc.selectFirst(".inactive-product-message") == null);
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst(".sku .product-data-value");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst(".sku-title h1");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst(".priceView-hero-price .sr-only");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Best Buy";
  }

  @Override
  public String getShipment() {
    Element shippingEL = doc.selectFirst("h5[itemProp='price']");
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
  public String getBrand() {
    Element val = doc.selectFirst("a.btn-brand-link");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(doc.select(".specs-table ul"), "li .row-title", "li .row-value");
  }

}
