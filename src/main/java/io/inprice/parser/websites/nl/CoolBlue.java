package io.inprice.parser.websites.nl;

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
 * Parser for CoolBlue the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CoolBlue extends AbstractWebsite {

  /*
   * holds price info set in getJsonData()
   */
  private JSONObject offers;

  public CoolBlue(Competitor competitor) {
    super(competitor);
  }

  @Override
  public JSONObject getJsonData() {
    Element dataEL = doc.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData().replace("\r\n", " "));
      if (data.has("offers")) {
        offers = data.getJSONObject("offers");
      }
      return data;
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      String availability = offers.getString("availability");
      return availability.contains("InStock") || availability.contains("PreOrder");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
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
    if (isAvailable()) {
      if (offers != null && offers.has("price")) {
        return offers.getBigDecimal("price");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "CoolBlue";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getJSONObject("brand").getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("span.js-delivery-information-usp");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getKeyValueSpecList(doc.select("div.product-specs__list-item.js-product-specs--list-item"),
        ".product-specs__item-title", ".product-specs__item-spec");
  }

}
