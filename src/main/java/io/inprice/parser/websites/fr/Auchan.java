package io.inprice.parser.websites.fr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Auchan France
 *
 * Contains json data placed in html. So, all data is extracted from json
 *
 * @author mdpinar
 */
public class Auchan extends AbstractWebsite {
	
	private Document dom;
	private JSONObject json;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

		String prodData = findAPart(html, "var product = ", "};", 1);
    if (prodData != null) json = new JSONObject(prodData);
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("productAvailability")) {
      return json.getBoolean("productAvailability");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("code")) {
      return json.getString("code");
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
      if (price.has("value")) {
        return price.getBigDecimal("value");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brandName")) {
      if (!"null".equals(json.get("brandName").toString())) {
        return json.getString("brandName");
      }
    }

    Element brand = dom.selectFirst("meta[itemprop='brand']");
    if (brand != null) {
      return brand.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    if (json != null && json.has("vendor")) {
      JSONObject vendor = json.getJSONObject("vendor");
      if (vendor.has("merchantName")) {
        return vendor.getString("merchantName");
      }
    }
    return "Auchan";
  }

  @Override
  public String getShipment() {
    Element shipping = dom.selectFirst("li.product-deliveryInformations--deliveryItem");
    if (shipping != null) {
      return shipping.text();
    }

    return "In-store pickup";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (json != null && json.has("extendedDescription")) {
      String desc = json.get("extendedDescription").toString();
      if (StringUtils.isNotBlank(desc)) {
        specList = new ArrayList<>();
        String[] descChunks = desc.split("<br/>");
        for (String dsc : descChunks) {
          specList.add(new LinkSpec("", dsc));
        }
      }
    }

    return specList;
  }

}
