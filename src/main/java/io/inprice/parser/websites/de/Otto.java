package io.inprice.parser.websites.de;

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
 * Parser for Otto Deutschland
 *
 * @author mdpinar
 */
public class Otto extends AbstractWebsite {

	private Document dom;

	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
	}

  @Override
  public boolean isAvailable() {
  	Element val = dom.selectFirst("link[itemprop='availability']");
    if (val != null) {
      String href = val.attr("href");
      return href.contains("InStock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null) {
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
    if (val != null) {
      return val.text();
    }
    val = dom.getElementById("brand");
    if (val != null) {
      return val.attr("data-brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    return "Otto";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("span.prd_price__note");
    if (val != null) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(dom.select("table.dv_characteristicsTable tr"), "td:nth-child(1)", "td:nth-child(2)");
  }

}
