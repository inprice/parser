package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * MediaMarkt Global Type 2
 *
 * URL depends on the country
 * NL --> https://www.mediamarkt.nl
 * TR --> https://www.mediamarkt.com.tr
 *
 * @author mdpinar
 */
public class MediaMarktXX_2 extends AbstractWebsite {

	private Document dom;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	protected ParseStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element metaUrl = dom.selectFirst("meta[property='og:url']");
		if (metaUrl != null && metaUrl.hasAttr("content") && metaUrl.attr("content").contains("error404")) {
			return ParseStatus.PS_NOT_FOUND;
		}
		return ParseStatus.PS_OK;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[itemprop='availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
    	String content = val.attr("content").toLowerCase();
      return (content.contains("instock") || content.contains("preorder"));
    }

    return false;
  }

  @Override
  public String getSku(String url) {
    Element val = dom.selectFirst("dd span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element shipment = dom.selectFirst("div.price.big");
    if (shipment != null) {
      Element val = shipment.nextElementSibling().selectFirst("small");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    shipment = dom.selectFirst("div.old-price-block");
    if (shipment != null) {
      Element val = shipment.nextElementSibling().selectFirst("small");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select("dl.specification"), "dt", "dd");
  }

}
