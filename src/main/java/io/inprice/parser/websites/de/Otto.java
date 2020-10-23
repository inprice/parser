package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Otto Deutschland
 *
 * The parsing steps:
 *
 * Three types of data is used for extracting all the info: 
 *   a) html body (specList) 
 *   b) json object extracted from html body in a script tag (brand) 
 *   c) product object set in getJsonData() by using json object in step b
 *
 * - the html body of link's url contains data (in json format) we need 
 * - in getJsonData(), we get that json data placed in a specific script tag 
 * - this data is named as product which is hold on a class-level variable
 *
 * @author mdpinar
 */
public class Otto extends AbstractWebsite {

  /*
   * the main data provider derived from json placed in html
   */
  private JSONObject product;

  @Override
  public JSONObject getJsonData() {
    Element val = doc.selectFirst("script#productDataJson");
    if (val != null) {
      JSONObject result = new JSONObject(val.dataNodes().get(0).getWholeData());
      if (!result.isEmpty() && result.has("variations")) {
        JSONObject variations = result.getJSONObject("variations");
        Set<String> keySet = variations.keySet();
        if (keySet != null && keySet.size() > 0) {
          product = variations.getJSONObject(keySet.iterator().next());
        }
      }
      return result;
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (product != null && product.has("availability")) {
      JSONObject var = product.getJSONObject("availability");
      if (var.has("status")) {
        return "available".equals(var.getString("status")) || "delayed".equals(var.getString("status"));
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    if (json != null && json.has("id")) {
      return json.getString("id");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null && product.has("name")) {
      return product.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.getElementById("reducedPriceAmount");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.getElementById("normalPriceAmount");
    }

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    if (product != null && product.has("displayPrice")) {
      JSONObject var = product.getJSONObject("displayPrice");
      if (var.has("techPriceAmount")) {
        return var.getBigDecimal("techPriceAmount");
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Otto";
  }

  @Override
  public String getShipment() {
    if (product != null && product.has("availability")) {
      JSONObject var = product.getJSONObject("availability");
      if (var.has("displayName")) {
        return var.getString("displayName");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getString("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("ul.prd_unorderedList li"));
  }

}
