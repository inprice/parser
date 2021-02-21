package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;
import kong.unirest.HttpResponse;

/**
 * Parser for Target USA
 *
 * The parsing steps:
 *
 *  - the html body of link's url contains data (in json format) we need 
 *  - in getJsonData(), we get that json data by using substring() method of String class 
 *  - this data is named as product which is hold on a class-level variable
 *  - each data (except for availability and specList) can be gathered using product variable
 *
 * @author mdpinar
 */
public class Target extends AbstractWebsite {

  private JSONObject preLoad;
  private JSONObject product;
  private JSONObject priceData;

  private void setPreLoadData() {
    final String preData = findAPart(doc.html(), "__PRELOADED_STATE__= ", "</script>");

    if (preData != null) {
      preLoad = new JSONObject(preData);
      if (preLoad.has("product")) {
        JSONObject prod = preLoad.getJSONObject("product");
        if (prod.has("productDetails")) {
          JSONObject details = prod.getJSONObject("productDetails");
          if (details.has("item")) {
            product = details.getJSONObject("item");
          }
        }
      }
    }
  }

  private String getApiKey() {
    if (preLoad != null && preLoad.has("config")) {
      JSONObject config = preLoad.getJSONObject("config");
      if (config.has("firefly")) {
        return config.getJSONObject("firefly").getString("apiKey");
      }
    }
    return null;
  }

  /**
   * Returns payload as key value query string
   *
   * @return String - as query parameter payload
   */
  private String getPayload() {
    StringBuilder sb = new StringBuilder();
    sb.append("pricing_store_id=1531");
    sb.append("&");
    sb.append("key=");
    sb.append(getApiKey());
    return sb.toString();
  }

  @Override
  protected JSONObject getJsonData() {
    setPreLoadData();
    return super.getJsonData();
  }

  private void setPriceData() {
    HttpResponse<String> response = httpClient
        .get("https://redsky.target.com/web/pdp_location/v1/tcin/" + getSku() + "?" + getPayload());
    if (response != null && response.getStatus() < 400) {
      JSONObject priceEL = new JSONObject(response.getBody());
      if (!priceEL.isEmpty()) {
        priceData = priceEL.getJSONObject("price");
      }
    }
  }

  @Override
  public boolean isAvailable() {
    if (product != null) {
      JSONObject available = null;
      if (product.has("available_to_promise_network")) {
        available = product.getJSONObject("available_to_promise_network");
      } else if (product.has("children")) {
        JSONObject children = product.getJSONObject("children");
        if (!children.isEmpty()) {
          Iterator<String> keys = children.keys();
          if (keys.hasNext()) {
            JSONObject firstChild = children.getJSONObject(keys.next());
            available = firstChild.getJSONObject("available_to_promise_network");
          }
        }
      }
      if (available != null) {
        return available.has("availability_status") && "IN_STOCK".equals(available.getString("availability_status"));
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    if (product != null && product.has("productId")) {
      return product.getString("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null && product.has("title")) {
      return product.getString("title");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (product != null && product.has("price")) {
      JSONObject priceEL = product.getJSONObject("price");
      if (priceEL.has("price")) {
        String price = priceEL.getString("price");
        if (StringUtils.isNotBlank(price))
          return new BigDecimal(cleanDigits(price));
      }
    }

    setPriceData();
    if (priceData != null) {
      if (priceData.has("current_retail")) {
        return priceData.getBigDecimal("current_retail");
      }
      if (priceData.has("current_retail_max")) {
        return priceData.getBigDecimal("current_retail_max");
      }
      if (priceData.has("current_retail_min")) {
        return priceData.getBigDecimal("current_retail_min");
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (product != null && product.has("productVendorName")) {
      String seller = product.getString("productVendorName");
      if (!seller.isEmpty())
        return seller;
    }
    return "Target";
  }

  @Override
  public String getShipment() {
    return "Free order pickup";
  }

  @Override
  public String getBrand() {
    if (product != null && product.has("manufacturerBrand")) {
      return product.getString("manufacturerBrand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (product != null && product.has("itemDetails")) {
      JSONObject details = product.getJSONObject("itemDetails");
      if (details.has("bulletDescription")) {
        JSONArray bullets = details.getJSONArray("bulletDescription");
        if (!bullets.isEmpty()) {
          specList = new ArrayList<>();
          for (int i = 0; i < bullets.length(); i++) {
            String spec = bullets.getString(i);
            String[] specChunks = spec.split("</B>");
            String key = "";
            String value;
            if (specChunks.length > 1) {
              key = specChunks[0];
              value = specChunks[1];
            } else {
              value = specChunks[0];
            }
            specList.add(new LinkSpec(key.replaceAll(":|<B>", ""), value));
          }
        }
      }
    }
    return specList;
  }

}
