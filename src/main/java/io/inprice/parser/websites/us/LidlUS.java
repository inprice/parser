package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.AbstractWebsite;
import kong.unirest.HttpResponse;

/**
 * Parser for Lidl USA
 *
 * Contains standard data, all is extracted from json data gotten from a special
 * url
 *
 * @author mdpinar
 */
public class LidlUS extends AbstractWebsite {

  private final String url = "https://mobileapi.lidl.com/v1/products/";

  private String sku;
  private String testLinkUrl;

  public LidlUS() {
    super();
  }

  public LidlUS(String testLinkUrl) {
    this.testLinkUrl = testLinkUrl;
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
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (json != null && json.has("longDescription")) {
      String desc = json.getString("longDescription");
      if (StringUtils.isNotBlank(desc)) {
        String features = desc.replaceAll("<ul>|</ul>|<li>", "");
        String[] featureChunks = features.split("</li>");
        if (featureChunks.length > 0) {
          specList = new ArrayList<>();
          for (String val : featureChunks) {
            if (StringUtils.isNotBlank(val) && !val.startsWith("<p>") && !val.startsWith("<div>"))
              specList.add(new LinkSpec("", val));
          }
        }
      }

    }

    return specList;
  }

  @Override
  protected Link getTestLink() {
    return new Link(this.testLinkUrl);
  }

  @Override
  public String getSiteName() {
  	return "lidl";
  }

  @Override
	public Country getCountry() {
		return Consts.Countries.US;
	}

}
