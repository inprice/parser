package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.StringHelper;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Euronics, United Kingdom
 *
 * https://www.euronics.co.uk
 *
 * @author mdpinar
 */
public class EuronicsUK extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (CollectionUtils.isNotEmpty(dataEL)) {
    	for (DataNode dNode : dataEL.dataNodes()) {
    		JSONObject data = new JSONObject(StringHelper.escapeJSON(dNode.toString()).replaceAll("\"description\".*(\"offers\":)", "$1"));
        if (data.has("@type")) {
          String type = data.getString("@type");
          if (type.equals("Product")) {
          	json = data;
            if (json.has("offers")) {
            	Object offersObj = json.get("offers");
            	if (offersObj instanceof JSONObject) {
            		offers = json.getJSONObject("offers");
            	} else {
            		JSONArray offersArr = (JSONArray) offersObj;
            		offers = offersArr.getJSONObject(0);
            	}
          		return OK_Status();
            }
          }
        }
      }
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    return true;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (offers != null && offers.has("price")) {
      return offers.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getString("brand");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
  	return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("table.classifications tr"), "td:nth-child(1)", "td:nth-child(2)");
  }

}
