package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.CharSetUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.DateUtils;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for AppliancesOnline Australia
 *
 * There is no way to fetch product data over html body.
 * So, we need to make another two request; one for product info and other one is for specifications!
 *
 * @author mdpinar
 */
public class AppliancesOnline extends AbstractWebsite {

	private JSONObject json;
	private JSONObject specsObj;

  @Override
  protected String getAlternativeUrl() {
    final String indicator = "product/";
    String productName = getUrl().substring(getUrl().indexOf(indicator) + indicator.length());
    return "https://www.appliancesonline.com.au/api/v2/product/slug/" + productName + "?date=" + DateUtils.formatAOLDate();
  }

	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		Document dom = Jsoup.parse(html);
		Element jsonEl = dom.getElementById("json");
		this.json = new JSONObject(CharSetUtils.squeeze(jsonEl.text().replaceAll("\n", " ")));
	}

  /**
   * We need an extra call to collect specifications!
   */
  @Override
  protected String getExtraUrl() {
  	return "https://www.appliancesonline.com.au/api/v2/product/specifications/id/" + getSku();
  }

  @Override
	protected void setExtraHtml(String html) {
		Document dom = Jsoup.parse(html);
		Element jsonEl = dom.getElementById("json");
		specsObj = new JSONObject(CharSetUtils.squeeze(jsonEl.text().replaceAll("\n", " ")));
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
  public String getShipment() {
    return "Check delivery cost";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    if (specsObj != null && specsObj.has("groupedAttributes")) {
      JSONObject groupedAttributes = specsObj.getJSONObject("groupedAttributes");
      if (!groupedAttributes.isEmpty()) {

        specs = new HashSet<>();
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

                specs.add(new LinkSpec(name, value.replaceAll("&#9679; ", "& ")));
              }
            }
          }
        }
      }
    }

    return specs;
  }
  
}
