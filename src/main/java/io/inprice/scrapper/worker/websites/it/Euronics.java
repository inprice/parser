package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Euronics Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Euronics extends AbstractWebsite {

  private Element base;

  public Euronics(Competitor competitor) {
    super(competitor);
  }

  @Override
  protected JSONObject getJsonData() {
    base = doc.getElementsByTag("trackingProduct").first();
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("span.productDetails__availability.not-available");
    return (val == null || StringUtils.isBlank(val.text()));
  }

  @Override
  public String getSku() {
    if (base != null) {
      return base.attr("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (base != null) {
      return base.attr("productName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (base != null) {
      return new BigDecimal(cleanDigits(base.attr("price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Euronics";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("span.productDetails__label.productDetails__label--left");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      StringBuilder sb = new StringBuilder();

      sb.append(val.text());
      sb.append(" ");

      val = doc.selectFirst("span.productDetails__label.productDetails__label--right");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        sb.append(val.text());
      }

      return sb.toString();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (base != null) {
      return base.attr("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("ul.productDetails__specifications li"));
  }
}
