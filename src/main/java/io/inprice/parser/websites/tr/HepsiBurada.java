package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
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

	private boolean isAvailable;
	private boolean isFreeShipping;

	protected Renderer getRenderer() {
		return Renderer.HEADLESS;
	}

	bu olmamış, düzeltilsin bilader
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

    String found = findAPart(html, "\"isInStock\":", ",");
    isAvailable = ("true".equalsIgnoreCase(found));
    
    found = findAPart(html, "\"freeShipping\":", ",");
    isFreeShipping = ("true".equalsIgnoreCase(found));
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("#addToCartForm input[name='sku']");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
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
    Element val = dom.selectFirst("label.campaign-text span");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Ücretsiz Kargo: " + (isFreeShipping ? "Evet" : "Hayır");
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select(".data-list.tech-spec tr"), "th", "td");
  }

}
