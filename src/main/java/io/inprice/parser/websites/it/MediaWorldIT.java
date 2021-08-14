package io.inprice.parser.websites.it;

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
 * MediaWorld, Italy
 *
 * https://www.mediaworld.it
 *
 * @author mdpinar
 */
public class MediaWorldIT extends AbstractWebsite {

	private Document dom;
	private Element prod;
	
	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}
	
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		String title = dom.title();
		if (title.toLowerCase().contains("error 404") == false) {
  		prod = dom.selectFirst("div.product-detail-main-container");
  		return HttpStatus.OK;
    }
		return HttpStatus.NOT_FOUND;
	}
	
  @Override
  public boolean isAvailable() {
    if (prod != null) {
      return (StringUtils.isNotBlank(prod.attr("data-gtm-avail2")) && prod.attr("data-gtm-avail2").contains("disponibile"));
    }
    return false;
  }

  @Override
  public String getSku() {
    if (prod != null) {
      return prod.attr("data-pcode");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (prod != null) {
      Element val = prod.selectFirst("h1[itemprop='name']");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        StringBuilder sb = new StringBuilder(val.text());
        Element descEL = prod.selectFirst("h4.product-short-description");
        if (descEL != null && StringUtils.isNotBlank(descEL.text())) {
          sb.append(" (");
          sb.append(descEL.text().split("\\|")[0].trim());
          sb.append(")");
        }
        return sb.toString();
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (prod != null) {
      return new BigDecimal(cleanDigits(prod.attr("data-gtm-price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (prod != null) {
      return prod.attr("data-gtm-brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("p.product-info-shipping");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select("li.content__Tech__row"), "div.Tech-row__inner__key", "div.Tech-row__inner__value");
  }

}
