package io.inprice.parser.websites.uk;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for NewLook UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class NewLook extends AbstractWebsite {

  public NewLook(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("meta[itemprop='availability']");
    if (val != null) {
      return val.attr("content").trim().equals("inStock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("li.active.list__item span[property='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "NewLook";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("span.product-delivery-link a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("section[itemprop='brand'] meta[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-details--description.cms p"));
  }
}
