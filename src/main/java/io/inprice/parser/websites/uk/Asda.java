package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;
import kong.unirest.HttpResponse;

/**
 * Parser for Asda UK
 *
 * Contains json data placed in html. So, all data is extracted from json
 *
 * @author mdpinar
 */
public class Asda extends AbstractWebsite {

  private JSONObject product;

  /**
   * Returns json object which holds all the essential data
   *
   * @return json - product data
   */
  @Override
  public JSONObject getJsonData() {
    String[] urlChunks = getUrl().split("/");

    if (urlChunks.length > 1) {
      String productId = urlChunks[urlChunks.length - 1];
      if (productId.matches("\\d+")) {
        HttpResponse<String> response = httpClient.get("https://groceries.asda.com/api/items/view?itemid=" + productId);
        if (response.getStatus() == 200 && StringUtils.isNotBlank(response.getBody())) {
          JSONObject prod = new JSONObject(response.getBody());
          if (prod.has("items")) {
            JSONArray items = prod.getJSONArray("items");
            if (items.length() > 0) {
              product = items.getJSONObject(0);
            }
          }
        }
      }
    }

    return null;
  }

  @Override
  public boolean isAvailable() {
    if (product != null && product.has("availability")) {
      return "A".equals(product.getString("availability"));
    }
    return false;
  }

  @Override
  public String getSku() {
    if (product != null && product.has("id")) {
      return product.getString("id");
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
    if (product != null && product.has("price")) {
      return new BigDecimal(cleanDigits(product.getString("price")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Asda";
  }

  @Override
  public String getShipment() {
    return "In-store pickup.";
  }

  @Override
  public String getBrand() {
    if (product != null && product.has("brandName")) {
      return product.getString("brandName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return null;
  }

}