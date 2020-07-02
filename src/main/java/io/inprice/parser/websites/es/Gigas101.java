package io.inprice.parser.websites.es;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for 101Gigas Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Gigas101 extends AbstractWebsite {

  public Gigas101(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("meta[property='product:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content").trim().contains("instock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("meta[property='product:retailer_part_no']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[property='product:sale_price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "101Gigas";
  }

  @Override
  public String getShipment() {
    Elements vals = doc.select("div.availability div#codigosku");
    if (vals != null && !vals.isEmpty()) {
      for (int i = 0; i < vals.size(); i++) {
        Element note = vals.get(i);
        if (note.text().contains("Envío")) {
          return note.text();
        }
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    Elements specs = doc.select("div#desc_prop tr");

    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        Element key = spec.selectFirst("td");
        Element value = key.nextElementSibling();
        specList.add(new CompetitorSpec(key.text(), value.text()));
      }
    }

    return specList;
  }
}
