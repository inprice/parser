package io.inprice.parser.websites.it;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for EPrice Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class EPrice extends AbstractWebsite {

  public EPrice(Link link) {
    super(link);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("meta[itemprop='availability']");
    if (val != null) {
      return val.attr("content").contains("InStock");
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
    Element val = doc.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("p.infoSeller a strong");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "ePrice";
  }

  @Override
  public String getShipment() {
    return "Venduto e spedito da " + getSeller();
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("meta[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = getKeyValueSpecList(doc.select("#anchorCar li"), "span", "a");
    if (specList == null) {
      Elements specs = doc.select("#anchorTech li");
      if (specs != null && specs.size() > 0) {
        specList = new ArrayList<>();
        for (Element spec : specs) {
          Elements pair = spec.select("span");
          if (pair.size() == 1) {
            specList.add(new LinkSpec("", pair.get(0).text()));
          } else if (pair.size() > 1) {
            specList.add(new LinkSpec(pair.get(0).text(), pair.get(1).text()));
          }
        }
      }
    }

    if (specList == null) {
      Elements specs = doc.select("#anchorDesc p");
      if (specs != null && specs.size() > 0) {
        specList = new ArrayList<>();
        for (Element spec : specs) {
          String[] specChunks = spec.text().split("\\.");
          for (String sp : specChunks) {
            specList.add(new LinkSpec("", sp));
          }
        }
      }
    }

    return specList;
  }
}
