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

  private JSONObject json;

  /**
   * Returns json object which holds all the essential data
   *
   * @return json - product data
   */
  @Override
  public void getJsonData() {
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
              json = items.getJSONObject(0);
            }
          }
        }
      }
    }
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("availability")) {
      return "A".equals(json.getString("availability"));
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("id")) {
      return json.getString("id");
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
      return new BigDecimal(cleanDigits(json.getString("price")));
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
    if (json != null && json.has("brandName")) {
      return json.getString("brandName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return null;
  }

}
