package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Zavvi UK
 *
 * Some parts of data place in html body, some in json data which is also in
 * html, and a significant part of them is in json which is in a script tag
 *
 * @author mdpinar
 */
public class Zavvi extends AbstractWebsite {

  /*
   * the main data provider derived from json placed in html
   */
  private JSONObject product;

  /**
   * Returns some info of the product as json
   *
   * @return json - partially has product data
   */
  @Override
  public JSONObject getJsonData() {
    Element dataEL = doc.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());

      if (data.has("offers")) {
        JSONArray offersArray = data.getJSONArray("offers");
        if (!offersArray.isEmpty()) {
          if (offersArray.getJSONObject(0).has("sku")) {
            product = offersArray.getJSONObject(0);
          }
        }
      }

      return data;
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    /*
    Element val = doc.selectFirst("p.productStockInformation_prefix");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().contains("In stock");
    }
    */
    return doc.html().indexOf("'productStatus':'Available'") >= 0;
  }

  @Override
  public String getSku() {
    if (product != null) {
      return product.getString("sku");
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
    if (product != null) {
      return product.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Zavvi";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div.productDeliveryAndReturns_message");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getJSONObject("brand").getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;
    Elements specs = doc.select("div.productDescription_contentWrapper");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        Element key = spec.selectFirst("div.productDescription_contentPropertyName span");
        Element value = spec.selectFirst("div.productDescription_contentPropertyValue");

        String strKey = null;
        String strValue = null;

        if (key != null) {
          strKey = key.text().replaceAll(":", "");
        }
        if (value != null)
          strValue = value.text();

        specList.add(new LinkSpec(strKey, strValue));
      }
    }
    return specList;
  }

}
