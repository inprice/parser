package io.inprice.parser.websites.nl;

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
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Wehkamp the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Wehkamp extends AbstractWebsite {

  /*
   * holds price info set in getJsonData()
   */
  private JSONObject offers;
  private JSONArray properties;

  @Override
  public JSONObject getJsonData() {
    final String props = findAPart(doc.html(), "\"properties\":", "]", 1);

    if (props != null) {
      properties = new JSONArray(props);
    }

    Elements dataEL = doc.select("script[type='application/ld+json']");
    if (dataEL != null && dataEL.size() > 0) {
      for (int i = 0; i < dataEL.size(); i++) {
        if (dataEL.get(i).dataNodes().get(0).getWholeData().indexOf("brand") > 0) {
          JSONObject data = new JSONObject(dataEL.get(i).dataNodes().get(0).getWholeData().replace("\r\n", " "));
          if (data.has("offers")) {
            offers = data.getJSONObject("offers");
          }
          return data;
        }
      }
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      String availability = offers.getString("availability");
      return availability.contains("InStock") || availability.contains("PreOrder");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
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
    if (isAvailable()) {
      if (offers != null && offers.has("price")) {
        return offers.getBigDecimal("price");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (offers != null && offers.has("seller")) {
      JSONObject seller = offers.getJSONObject("seller");
      if (seller.has("name")) {
        return seller.getString("name");
      }
    }
    return "Wehkamp";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getJSONObject("brand").getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("span.margin-vertical-xsmall.font-weight-light");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (properties != null && properties.length() > 0) {
      specList = new ArrayList<>();
      for (int i = 0; i < properties.length(); i++) {
        JSONObject prop = properties.getJSONObject(i);
        specList.add(new LinkSpec(prop.getString("label"), prop.getString("value")));
      }
    }

    return specList;
  }

  @Override
  public String getSiteName() {
  	return "wehkamp";
  }

  @Override
	public Country getCountry() {
		return Consts.Countries.NL;
	}

}
