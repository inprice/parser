package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * BestBuy, USA
 * 
 * https://www.bestbuy.com
 *
 * @author mdpinar
 */
public class BestBuyUS extends AbstractWebsite {

	private Document dom;

	@Override
	protected By clickFirstBy() {
		return By.className("us-link");
	}
	
	@Override
	protected By waitBy() {
		return By.className("priceView-price-match-guarantee");
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().toLowerCase().contains("not found") == false) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
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
    Element val = dom.selectFirst(".priceView-price-match-guarantee + div .priceView-customer-price > span:nth-child(1)");
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
    	}
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select(".specs-table ul"), "li .row-title", "li .row-value");
  }

}
