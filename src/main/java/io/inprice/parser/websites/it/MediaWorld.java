package io.inprice.parser.websites.it;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for MediaWorld Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaWorld extends AbstractWebsite {

  private Element product;

  public MediaWorld(Competitor competitor) {
    super(competitor);
  }

  @Override
  protected JSONObject getJsonData() {
    product = doc.selectFirst("div.product-detail-main-container");
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (product != null) {
      return (StringUtils.isNotBlank(product.attr("data-gtm-avail2")) && product.attr("data-gtm-avail2").contains("disponibile"));
    }
    return false;
  }

  @Override
  public String getSku() {
    if (product != null) {
      return product.attr("data-pcode");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null) {
      Element val = product.selectFirst("h1[itemprop='name']");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        StringBuilder sb = new StringBuilder(val.text());
        Element descEL = product.selectFirst("h4.product-short-description");
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
    if (product != null) {
      return new BigDecimal(cleanDigits(product.attr("data-gtm-price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Media World";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("p.product-info-shipping");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (product != null) {
      return product.attr("data-gtm-brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getKeyValueSpecList(doc.select("li.content__Tech__row"), "div.Tech-row__inner__key",
        "div.Tech-row__inner__value");
  }
}
