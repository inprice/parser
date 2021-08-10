package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Debenhams UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Debenhams extends AbstractWebsite {

	private Document dom;

  @Override
  protected By waitBy() {
  	return By.cssSelector("span[itemprop='name'], div[data-test-id='404-page-message']");
  }

	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    Element notFoundDiv = dom.selectFirst("div[data-test-id='404-page-message']");
    if (notFoundDiv == null) {
    	return HttpStatus.OK;
    }
    return HttpStatus.NOT_FOUND;
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
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("[data-test-id='product-title']");
    if (val != null) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
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
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Check delivery options";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select("[data-test-id='details-and-care']"));
  }

}
