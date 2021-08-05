package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.util.Set;

import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for UlaBox Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class UlaBox extends AbstractWebsite {

	private JSONObject json;
	
	@Override
	protected void setHtml(String html) {
		
		String rawJson = null;
		if (html.indexOf("}]},") > -1) {
			rawJson = findAPart(html, "\\\"product\\\":{\\\"product\\\":", "}]},", 3, 0);
		} else {
			rawJson = findAPart(html, "\\\"product\\\":{\\\"product\\\":", "}}\",", 2, 0);
		}
		rawJson = rawJson.replace("\\\"", "\"");
		rawJson = rawJson.replace(":\"{\"", ":{\"");
		rawJson = rawJson.replace("]}}\",", "]}},");
		rawJson = rawJson.replace("\\u003c", "<");
		rawJson = rawJson.replace("\\u003e", ">");
		rawJson = rawJson.replace("\\\"", "\"");
		
		json = new JSONObject(rawJson);
	}

  @Override
  public boolean isAvailable() {
		if (json != null && json.has("unitQuantity")) {
			return json.getInt("unitQuantity") > 0;
		}
    return false;
  }

  @Override
  public String getSku() {
		if (json != null && json.has("id")) {
			return ""+json.getLong("id");
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
  		JSONObject price = json.getJSONObject("price");
  		return price.getBigDecimal("value");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (json != null && json.has("brand")) {
			JSONObject brand = json.getJSONObject("brand");
			return brand.getString("name");
		}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return null;
  }

}
