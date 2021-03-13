package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Otto Deutschland
 *
 * The parsing steps:
 *
 * Three types of data is used for extracting all the info: 
 *   a) html body (specList) 
 *   b) json object extracted from html body in a script tag (brand) 
 *   c) product object set in getJsonData() by using json object in step b
 *
 * - the html body of link's url contains data (in json format) we need 
 * - in getJsonData(), we get that json data placed in a specific script tag 
 * - this data is named as product which is hold on a class-level variable
 *
 * @author mdpinar
 */
public class Otto extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject product;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

    Element val = dom.selectFirst("script#productDataJson");
    if (val != null) {
    	json = new JSONObject(val.dataNodes().get(0).getWholeData());
      if (json.has("variations")) {
        JSONObject variations = json.getJSONObject("variations");
        Set<String> keySet = variations.keySet();
        if (keySet != null && keySet.size() > 0) {
          product = variations.getJSONObject(keySet.iterator().next());
        }
      }
    }
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

  @Override
  public boolean isAvailable() {
    if (product != null && product.has("availability")) {
      JSONObject var = product.getJSONObject("availability");
      if (var.has("status")) {
        return "available".equals(var.getString("status")) || "delayed".equals(var.getString("status"));
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    if (json != null && json.has("id")) {
      return json.getString("id");
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
    Element val = dom.getElementById("reducedPriceAmount");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.getElementById("normalPriceAmount");
    }

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    if (product != null && product.has("displayPrice")) {
      JSONObject var = product.getJSONObject("displayPrice");
      if (var.has("techPriceAmount")) {
        return var.getBigDecimal("techPriceAmount");
      }
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
  public String getSeller() {
    return "Otto";
  }

  @Override
  public String getShipment() {
    if (product != null && product.has("availability")) {
      JSONObject var = product.getJSONObject("availability");
      if (var.has("displayName")) {
        return var.getString("displayName");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("ul.prd_unorderedList li"));
  }

}
