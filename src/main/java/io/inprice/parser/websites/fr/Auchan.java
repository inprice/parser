package io.inprice.parser.websites.fr;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Auchan France
 *
 * Contains json data placed in html. So, all data is extracted from json
 *
 * @author mdpinar
 */
public class Auchan extends AbstractWebsite {

  public Auchan(Competitor competitor) {
    super(competitor);
  }

  /**
   * Returns json object which holds all the necessity data
   *
   * @return json - product data
   */
  @Override
  public JSONObject getJsonData() {
    final String prodData = findAPart(doc.html(), "var product = ", "};", 1);

    if (prodData != null) {
      return new JSONObject(prodData);
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("productAvailability")) {
      return json.getBoolean("productAvailability");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("code")) {
      return json.getString("code");
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
      JSONObject price = json.getJSONObject("price");
      if (price.has("value")) {
        return price.getBigDecimal("value");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (json != null && json.has("vendor")) {
      JSONObject vendor = json.getJSONObject("vendor");
      if (vendor.has("merchantName")) {
        return vendor.getString("merchantName");
      }
    }
    return "Auchan";
  }

  @Override
  public String getShipment() {
    Element shipping = doc.selectFirst("li.product-deliveryInformations--deliveryItem");
    if (shipping != null) {
      return shipping.text();
    }

    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brandName")) {
      if (!"null".equals(json.get("brandName").toString())) {
        return json.getString("brandName");
      }
    }

    Element brand = doc.selectFirst("meta[itemprop='brand']");
    if (brand != null) {
      return brand.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    if (json != null && json.has("extendedDescription")) {
      String desc = json.get("extendedDescription").toString();
      if (StringUtils.isNotBlank(desc)) {
        specList = new ArrayList<>();
        String[] descChunks = desc.split("<br/>");
        for (String dsc : descChunks) {
          specList.add(new CompetitorSpec("", dsc));
        }
      }
    }

    return specList;
  }

}
