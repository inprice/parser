package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Target USA
 * 
 * https://www.target.com
 *
 * @author mdpinar
 */
public class TargetUS extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

  @Override
  protected By waitBy() {
  	return By.cssSelector("div[data-test='product-price']");
  }

	@Override
	protected ParseStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (CollectionUtils.isNotEmpty(dataEL)) {
    	for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject dataNode = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()).replaceAll("\"description\".*(\"gtin13\":)", "$1"));
        if (dataNode.has("@graph")) {
        	JSONArray nodesArr = dataNode.getJSONArray("@graph");
        	for (int i = 0; i < nodesArr.length(); i++) {
        		JSONObject data = nodesArr.getJSONObject(i);
        		if (data.has("@type") && data.getString("@type").equals("Product")) {
            	json = data;
              if (json.has("offers")) {
              	Object offersObj = json.get("offers");
              	if (offersObj instanceof JSONObject) {
              		offers = json.getJSONObject("offers");
              	} else {
              		JSONArray offersArr = (JSONArray) offersObj;
              		offers = offersArr.getJSONObject(0);
              	}
              	return ParseStatus.PS_OK;
              }
        		}
					}
        }
      }
    }
    return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      String availability = offers.getString("availability").toLowerCase();
      return availability.contains("instock") || availability.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku(String url) {
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
    Element val = dom.selectFirst("[data-test='product-price']");
    if (val != null) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getString("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Check delivery options";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select("[data-test='detailsTab'] li"));
  }

}
