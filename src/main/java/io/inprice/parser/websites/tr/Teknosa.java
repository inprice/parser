package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Teknosa Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Teknosa extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

		Element dataEL = dom.getElementById("schemaJSON");
    if (dataEL != null) {
      // user reviews may cause encoding problems and can be huge amount of data
      // and we do not need user opinions
      // thus, they are trimmed with the help of regex
      String rawJson = dataEL.html();
      rawJson = rawJson.replaceAll("(?s)\\s*\"review\":.*\\],", "");
      json = new JSONObject(rawJson);
      if (json.has("offers")) {
        offers = json.getJSONObject("offers");
      }
    }
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("offers")) {
      JSONArray subOffers = offers.getJSONArray("offers");
      if (subOffers.length() > 0 && subOffers.getJSONObject(0).has("availability")) {
        return subOffers.getJSONObject(0).getString("availability").contains("InStock");
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null) {
      if (json.has("sku")) {
        return json.getString("sku");
      }
      if (json.has("@id")) {
        String id = json.getString("@id");
        if (id != null)
          return cleanDigits(id);
      }
    }

    Element sku = dom.selectFirst("span.item-number-value");
    if (sku != null) {
      return sku.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }

    Element name = dom.selectFirst("div#ProductTitle span.title");
    if (name != null) {
      return name.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (isAvailable()) {
      if (offers != null) {
        if (!offers.isEmpty() && offers.has("lowPrice")) {
          return offers.getBigDecimal("lowPrice");
        } else if (!offers.isEmpty() && offers.has("price")) {
          return offers.getBigDecimal("price");
        }
      }
      
      Element price = dom.selectFirst("span.VersionOfferPrice img");
      if (price != null) {
        return new BigDecimal(cleanDigits(price.attr("alt")));
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Teknosa";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div.pw-dangerous-html.dbh-content");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.getElementById("hd3");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Mağazada teslim";
  }

  @Override
  public String getBrand() {
    if (json != null) {
      if (json.has("brand")) {
        String bn = json.getJSONObject("brand").getString("name");
        if (StringUtils.isNotBlank(bn))
          return bn;
      }
      if (json.has("schema:brand")) {
        String bn = json.getString("schema:brand");
        if (StringUtils.isNotBlank(bn))
          return bn;
      }
    }

    Element brand = dom.selectFirst("div.brand-name a");
    if (brand != null) {
      return brand.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specKeys = dom.select("div.product-classifications tr");
    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      for (Element key : specKeys) {
        Element val = key.selectFirst("td");
        specList.add(new LinkSpec(val.text(), ""));
      }
    }

    Elements specValues = dom.select("div.product-classifications tr");
    if (specValues != null && specValues.size() > 0) {
      boolean isEmpty = false;
      if (specList == null) {
        isEmpty = true;
        specList = new ArrayList<>();
      }
      for (int i = 0; i < specValues.size(); i++) {
        Element value = specValues.get(i);
        Element val = value.selectFirst("td span");
        if (isEmpty) {
          specList.add(new LinkSpec("", val.text()));
        } else {
          specList.get(i).setValue(val.text());
        }
      }
    }

    return specList;
  }

}
