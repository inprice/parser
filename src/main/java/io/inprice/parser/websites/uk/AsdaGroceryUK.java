package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * AsdaGrocery, United Kingdom
 *
 * https://groceries.asda.com
 *
 * @author mdpinar
 */
public class AsdaGroceryUK extends AbstractWebsite {

	private Document dom;

	@Override
	protected By waitBy() {
		return By.className("pdp-main-details__title");
	}
	
	@Override
	protected ParseStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().toLowerCase().contains("not found") == false) {
			return ParseStatus.PS_OK;
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	Element val = dom.selectFirst("button[data-auto-id='btnAdd']");
    return (val != null && val.hasAttr("disabled") == false);
  }

	@Override
	public String getSku(String url) {
		String[] chunks = url.split("/");
		if (chunks.length > 0) {
			return chunks[chunks.length-1];
		}
		return Consts.Words.NOT_AVAILABLE;
	}

	@Override
	public String getName() {
  	Element val = dom.selectFirst(".pdp-main-details__title");
    if (val != null) {
    	return val.text();
    }
		return Consts.Words.NOT_AVAILABLE;
	}

	@Override
	public BigDecimal getPrice() {
  	Element val = dom.selectFirst(".pdp-main-details__price");
    if (val != null) {
    	return new BigDecimal(cleanDigits(val.text()));
    }
		return BigDecimal.ZERO;
	}

	@Override
	public String getBrand() {
		return "Asda";
	}

	@Override
	public String getShipment() {
		return "Check delivery info";
	}

	@Override
	public Set<LinkSpec> getSpecs() {
		return getKeyValueSpecs(dom.select(".pdp-description-reviews__product-details-cntr"), ".pdp-description-reviews__product-details-title", ".pdp-description-reviews__product-details-content");
	}

}
