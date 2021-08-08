package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for HepsiBurada Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class HepsiBurada extends AbstractWebsite {

	private Document dom;

	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().toLowerCase().startsWith("404 sayfa") == false) {
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("link[itemprop='availability']");
    if (val != null && val.hasAttr("href")) {
    	String href = val.attr("href").toLowerCase();
      return (href.contains("instock") || href.contains("preorder"));
    }
    return false;
  }

  @Override
  public String getSku() {
  	String[] chunks = getUrl().split("-");
  	if (chunks.length > 0) {
  		return chunks[chunks.length-1];
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.getElementById("product-name");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst("span[itemprop='name']");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("span[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("span.seller a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    return "Kargo seçenekleri için bakınız";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select(".data-list.tech-spec tr"), "th", "td");
  }

}
