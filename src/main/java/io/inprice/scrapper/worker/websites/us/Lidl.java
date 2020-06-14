package io.inprice.scrapper.worker.websites.us;

import kong.unirest.HttpResponse;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Lidl USA
 *
 * Contains standard data, all is extracted from json data gotten from a special
 * url
 *
 * @author mdpinar
 */
public class Lidl extends AbstractWebsite {

  private final String url = "https://mobileapi.lidl.com/v1/products/";

  private String sku;

  public Lidl(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean willHtmlBePulled() {
    return false;
  }

  @Override
  public JSONObject getJsonData() {
    final int index = getUrl().indexOf("/products/");
    final String[] urlChunks = getUrl().split("/");

    if (index > 0 && urlChunks.length > 1) {
      sku = urlChunks[urlChunks.length - 1];
      HttpResponse<String> response = httpClient.get(url + sku);
      if (response.getStatus() < 400) {
        return new JSONObject(response.getBody());
      }
    }

    return null;
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("price")) {
      JSONObject price = json.getJSONObject("price");
      if (price.has("baseQuantity")) {
        return price.getJSONObject("baseQuantity").getInt("value") > 0;
      }
    }
    return true;
  }

  @Override
  public String getSku() {
    return sku;
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
      if (price.has("currentPrice")) {
        return price.getJSONObject("currentPrice").getBigDecimal("value");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Lidl";
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brands")) {
      JSONArray brands = json.getJSONArray("brands");
      if (!brands.isEmpty() && brands.length() > 0) {
        return brands.getString(0);
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    if (json != null && json.has("longDescription")) {
      String desc = json.getString("longDescription");
      if (StringUtils.isNotBlank(desc)) {
        String features = desc.replaceAll("<ul>|</ul>|<li>", "");
        String[] featureChunks = features.split("</li>");
        if (featureChunks.length > 0) {
          specList = new ArrayList<>();
          for (String val : featureChunks) {
            if (StringUtils.isNotBlank(val) && !val.startsWith("<p>") && !val.startsWith("<div>"))
              specList.add(new CompetitorSpec("", val));
          }
        }
      }

    }

    return specList;
  }
}
