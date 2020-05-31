package io.inprice.scrapper.worker.websites.au;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parser for AppliancesOnline Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class AppliancesOnline extends AbstractWebsite {

  public AppliancesOnline(Link link) {
    super(link);
  }

  @Override
  protected String getAlternativeUrl() {
    final String indicator = "product/";
    String productName = getUrl().substring(getUrl().indexOf(indicator) + indicator.length());
    return "https://www.appliancesonline.com.au/api/v2/product/slug/" + productName;
  }

  @Override
  protected JSONObject getJsonData() {
    if (doc != null)
      return new JSONObject(doc.body().html());
    return null;
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("available")) {
      return json.getBoolean("available");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("productId")) {
      return "" + json.getInt("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("title")) {
      return json.getString("title");
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
    return "Appliance Online";
  }

  @Override
  public String getShipment() {
    return "Check delivery cost";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("manufacturer")) {
      JSONObject manufacturer = json.getJSONObject("manufacturer");
      if (manufacturer.has("name")) {
        return manufacturer.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    HttpResponse<String> response = httpClient
        .get("https://www.appliancesonline.com.au/api/v2/product/specifications/id/" + json.getInt("productId"));

    if (response.getStatus() == 200 && StringUtils.isNotBlank(response.getBody())) {
      JSONObject specs = new JSONObject(response.getBody());
      if (specs.has("groupedAttributes")) {
        JSONObject groupedAttributes = specs.getJSONObject("groupedAttributes");
        if (!groupedAttributes.isEmpty()) {

          specList = new ArrayList<>();
          Iterator<String> keys = groupedAttributes.keys();

          while (keys.hasNext()) {
            String key = keys.next();
            JSONObject attrs = groupedAttributes.getJSONObject(key);
            if (attrs.has("attributes")) {

              JSONArray array = attrs.getJSONArray("attributes");

              if (array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                  JSONObject attr = array.getJSONObject(i);

                  String name = attr.getString("displayName");
                  String value = attr.getString("value");
                  String type = attr.getString("inputType");

                  if ("boolean".equals(type)) {
                    if ("1".equals(value))
                      value = "Yes";
                    else
                      value = "No";
                  }

                  specList.add(new LinkSpec(name, value));
                }
              }
            }
          }
        }
      }
    }

    return specList;
  }
}
