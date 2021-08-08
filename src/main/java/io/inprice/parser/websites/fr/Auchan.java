package io.inprice.parser.websites.fr;

import java.math.BigDecimal;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Auchan France
 *
 * @author mdpinar
 */
public class Auchan extends AbstractWebsite {
	
	private Document dom;
	
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element notFoundDiv = dom.selectFirst(".error404");
		if (notFoundDiv == null) {
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("link[itemprop='availability']");
    if (val != null && val.hasAttr("href")) {
      String href = val.attr("href").toLowerCase();
      return href.contains("instock") || href.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku() {
    String[] chunks = getUrl().split("-");
    return chunks[chunks.length-1].toUpperCase();
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[itemprop='price']");
    if (val != null) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val != null) {
    	return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst(".product-price--seller");
    if (val != null) {
    	return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element shipping = dom.selectFirst("li.product-deliveryInformations--deliveryItem");
    if (shipping != null) {
      return shipping.text().replace("Vendu et expédié par ", "");
    }

    return "In-store pickup";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select("ul.product-aside--list"), "span.product-aside--listSubtitle", "span.product-aside--listValue");
    if (specs != null && specs.size() > 0) return specs;

    specs = getValueOnlySpecs(dom.select(".product-aside--textBlock li"));
    if (specs != null && specs.size() > 0) return specs;

    return specs;
  }

}
