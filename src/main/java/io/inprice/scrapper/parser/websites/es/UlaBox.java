package io.inprice.scrapper.parser.websites.es;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.parser.helpers.Consts;
import io.inprice.scrapper.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for UlaBox Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class UlaBox extends AbstractWebsite {

  public UlaBox(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("div.product-shop");
    if (val != null && StringUtils.isNotBlank(val.attr("data-product-qty"))) {
      try {
        int qty = new Integer(cleanDigits(val.attr("data-product-qty")));
        return qty > 0;
      } catch (Exception ignored) { }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("div.product-shop");
    if (val != null && StringUtils.isNotBlank(val.attr("data-product-id"))) {
      return val.attr("data-product-id");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("div.product-shop");
    if (val != null && StringUtils.isNotBlank(val.attr("data-product-name"))) {
      return val.attr("data-product-name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("div.product-shop");
    if (val != null && StringUtils.isNotBlank(val.attr("data-price"))) {
      return new BigDecimal(cleanDigits(val.attr("data-price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "UlaBox";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div.value-description");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("span.milli a.js-pjax");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return null;
  }
}
