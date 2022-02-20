package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * CanadianTire, Canada
 *
 * https://www.canadiantire.ca
 *
 * @author mdpinar
 */
public class CanadianTireCA extends AbstractWebsite {

	private Document dom;

  @Override
	protected String getWaitForSelector() {
		return ".price__total-value";
	}
  
	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().contains("404 |") == false) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	return (dom.selectFirst(".add-to-cart__button") != null);
  }

  @Override
  public String getSku() {
  	Element val = dom.selectFirst("div[data-bv-product-id]");
  	if (val != null && val.hasAttr("data-bv-product-id")) {
  		return val.attr("data-bv-product-id");
  	}
		return "NA";
  }

  @Override
  public String getName() {
    Element name = dom.selectFirst(".js-product-name");
    if (name != null) {
      return name.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element price = dom.selectFirst("span.price__total-value");
  	if (price == null) price = dom.selectFirst("span.price__reg-value");

  	if (price != null) {
      return new BigDecimal(cleanDigits(price.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element brand = dom.selectFirst("img.brand-logo-link__img");
    if (brand == null) brand = dom.selectFirst("img.brand-footer__logo");

    if (brand != null) {
      return brand.attr("alt");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select("div.pdp-details-features__items li"));
  }

}
