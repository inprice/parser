package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Currys UK
 *
 * Contains standard data, all is extracted via json data - product set in
 * getJsonData()
 *
 * @author mdpinar
 */
public class Currys extends AbstractWebsite {

  /*
   * the main data provider derived from json placed in html
   */
  private JSONObject product;

  @Override
  public JSONObject getJsonData() {
    Element dataEL = doc.getElementById("app.digitalData");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
      if (data.has("product") && !data.getJSONArray("product").isEmpty()) {
        product = data.getJSONArray("product").getJSONObject(0);
      }
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (product != null && product.has("stockStatus")) {
      String status = product.getString("stockStatus");
      return "In stock".equalsIgnoreCase(status);
    }
    return false;
  }

  @Override
  public String getSku() {
    if (product != null && product.has("productSKU")) {
      return product.getString("productSKU");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null && product.has("productName")) {
      return product.getString("productName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (product != null && product.has("currentPrice")) {
      return product.getBigDecimal("currentPrice");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Currys";
  }

  @Override
  public String getShipment() {
    Element val = doc.getElementById("delivery");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll("More info", "");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (product != null && product.has("manufacturer")) {
      return product.getString("manufacturer");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-highlight li"));
  }

}
