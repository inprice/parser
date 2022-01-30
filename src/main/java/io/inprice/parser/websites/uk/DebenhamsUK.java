package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Debenhams, United Kingdom
 *
 * https://www.debenhams.com
 *
 * @author mdpinar
 */
public class DebenhamsUK extends AbstractWebsite {

	private Document dom;

  @Override
  protected By waitBy() {
  	return By.cssSelector("span[itemprop='name'], div[data-test-id='404-page-message']");
  }

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

    Element notFoundDiv = dom.selectFirst("div[data-test-id='404-page-message']");
    if (notFoundDiv == null) {
    	return OK_Status();
    }
    return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("button[data-test-id='add-to-basket-button']");
    return (val != null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("span[data-test-id='product-sku']");
    if (val != null) {
      String[] chunks = val.text().split(":");
      return chunks[chunks.length-1];
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("[data-test-id='product-title']");
    if (val != null) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("[data-test-id='product-current-price']");
    if (val != null) {
    	val.select("span, div").remove();
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("[data-test-id='product-brand']");
    if (val != null) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
  	return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select("[data-test-id='details-and-care']"));
  }

}
