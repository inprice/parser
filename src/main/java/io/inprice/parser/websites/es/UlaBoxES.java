package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * UlaBox, Spain
 *
 * https://www.ulabox.com
 *
 * @author mdpinar
 */
public class UlaBoxES extends AbstractWebsite {

	private Document dom;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element notFoundImg = dom.selectFirst("img[alt$='Page not found']");
		if (notFoundImg == null) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("link[itemProp='availability']");
    if (val != null && val.hasAttr("href")) {
      String href = val.attr("href").toLowerCase();
      return href.contains("instock") || href.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[property='og:url']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
    	String[] chunks = val.attr("content").split("/");
      return chunks[chunks.length-1];
    }

    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element val = dom.selectFirst("meta[itemProp='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("a[itemProp='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("title"))) {
      return val.attr("title");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Check shipping conditions";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return null;
  }

}
