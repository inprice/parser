package io.inprice.parser.websites.au;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for BigW Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class BigW extends AbstractWebsite {

  private String brand = Consts.Words.NOT_AVAILABLE;
  private List<LinkSpec> specList;

  @Override
  protected JSONObject getJsonData() {
    specList = getKeyValueSpecList(doc.select("div.tab-Specification li"), "div.meta", "div.subMeta");
    for (LinkSpec spec : specList) {
      if (spec.getKey().contains("Brand")) {
        brand = spec.getValue();
        break;
      }
    }

    final String prodData = findAPart(doc.html(), "'products': [", "}]", 1);
    if (prodData != null) {
      return new JSONObject(fixQuotes(prodData));
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    Element increaeBtn = doc.getElementById("increase_quantity_JS");
    return (increaeBtn != null);
  }

  @Override
  public String getSku() {
    Element code = doc.selectFirst("div[data-productcode]");
    if (code != null) {
      return code.attr("data-productcode");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("price")) {
      return json.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Big W";
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    return brand;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return specList;
  }
}
