package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Argos, United Kingdom
 *
 * https://www.argos.co.uk
 *
 * @author mdpinar
 */
public class ArgosUK extends AbstractWebsite {

	private Document dom;

	private boolean isAvailable;
	private String brand;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	} 

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().toLowerCase().contains("went wrong") == false) {
	    String found = findAPart(html, "\"globallyOutOfStock\":", ",");
	    isAvailable = ("false".equalsIgnoreCase(found));
	    brand = findAPart(html, "\"brand\":\"", "\",");
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("[itemProp='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("span.product-title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("[data-test='product-title']");
    if (val != null && StringUtils.isNotBlank(val.html())) {
      return val.html();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst(".product-price-primary");
    if (val == null || StringUtils.isBlank(val.attr("content"))) val = dom.selectFirst("[itemProp='price']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (StringUtils.isNotBlank(brand)) return brand;

    Element val = dom.selectFirst("[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    String staticPart = "In-store pickup";

    Element val = dom.selectFirst("a.ac-propbar__slot > span.sr-only");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return staticPart + " OR " + val.text();
    }

    return staticPart;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select(".product-description-content-text li"));
  }

}
