package io.inprice.parser.websites.es;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Electroking Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Electroking extends AbstractWebsite {

  public Electroking(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("span[data-stock]");
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
    Element val = doc.selectFirst("span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = doc.selectFirst("p.product_name strong");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
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
  public String getBrand() {
    Element val = doc.selectFirst("div.product-manufacturer a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("img.manufacturer-logo");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      return val.attr("alt");
    }

    return getSeller();
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-description li"));
  }
}
