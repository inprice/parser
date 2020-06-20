package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Kogan Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Kogan extends AbstractWebsite {

  public Kogan(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("link[itemProp='availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("href"))) {
      return val.attr("href").contains("InStock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("p[itemProp='model']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
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
    return "Kogan";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div[itemprop='offers'] span[role='tooltip']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("meta[itemProp='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return "Kogan";
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("section[itemprop='description'] li"));
  }
}
