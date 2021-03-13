package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for The Good Guys Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class TheGoodGuys extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.getElementById("add2CartBtn");
    return (val != null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("span.titleItems_model_digit");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.getElementById("mainProductId");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1.pdp__main-title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='og:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("img.brand_logo_keyftrs");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      return val.attr("alt");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    return "TheGoodGuys";
  }

  @Override
  public String getShipment() {
    return "Check delivery cost";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(dom.select("section#keyftr li"), "small", "h2");
  }

}
