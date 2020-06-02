package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Trendyol Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Trendyol extends AbstractWebsite {

  public Trendyol(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("button.add-to-bs");
    return (val != null);
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("competitor[rel='canonical']");
    if (val != null && StringUtils.isNotBlank(val.attr("href"))) {
      String[] competitorChunks = val.attr("href").split("-");
      if (competitorChunks.length > 0) {
        return competitorChunks[competitorChunks.length - 1];
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("meta[name='twitter:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = doc.selectFirst("pr-nm");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[name='twitter:data1']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("span.pr-in-dt-spn");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("meta[name='twitter:description']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      String[] sellerChunks = val.attr("content").split(":");
      if (sellerChunks.length > 0) {
        return sellerChunks[sellerChunks.length - 1];
      }
    }

    return "Trendyol";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div.stamp.crg div");
    if (val != null) {
      return "Kargo Bedava";
    }

    val = doc.selectFirst("span.pr-in-dt-spn");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().trim() + " tarafından gönderilecektir.";
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("div.pr-in-cn div.pr-in-br a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return getSeller();
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    Elements specs = doc.select("div.pr-in-dt-cn ul span li");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String[] specChunks = spec.text().split("\\.");
        for (String sp : specChunks) {
          specList.add(new CompetitorSpec("", sp));
        }
      }
    }
    return specList;
  }

}
