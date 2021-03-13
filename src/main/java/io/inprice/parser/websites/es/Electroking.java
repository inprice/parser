package io.inprice.parser.websites.es;

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
 * Parser for Electroking Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Electroking extends AbstractWebsite {

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
    Element val = dom.selectFirst("span[data-stock]");
    if (val != null && StringUtils.isNotBlank(val.attr("data-stock"))) {
      try {
        int amount = new Integer(cleanDigits(val.attr("data-stock")));
        return (amount > 0);
      } catch (Exception e) {
        //
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("span[itemprop='sku']");
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

    val = dom.selectFirst("p.product_name strong");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
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
    Element val = dom.selectFirst("div.product-manufacturer a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("img.manufacturer-logo");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      return val.attr("alt");
    }

    return getSeller();
  }

  @Override
  public String getSeller() {
    return "Electroking";
  }

  @Override
  public String getShipment() {
    return "Envío por Agencia de Transporte. Ver detalles";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("div.product-description li"));
  }

}
