package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
 * Parser for Zavvi UK
 *
 * Some parts of data place in html body, some in json data which is also in
 * html, and a significant part of them is in json which is in a script tag
 *
 * @author mdpinar
 */
public class Zavvi extends AbstractWebsite {

	private Document dom;

	private boolean isAvailable;
	private JSONObject json;
  private JSONObject prod;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

		dom = Jsoup.parse(html);
		isAvailable = (html.indexOf("'productStatus':'Available'") >= 0);

    Element dataEL = dom.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
    	json = new JSONObject(dataEL.dataNodes().get(0).getWholeData());

      if (json.has("offers")) {
        JSONArray offersArray = json.getJSONArray("offers");
        if (!offersArray.isEmpty()) {
          if (offersArray.getJSONObject(0).has("sku")) {
            prod = offersArray.getJSONObject(0);
          }
        }
      }
    }
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    if (prod != null) {
      return prod.getString("sku");
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
    if (prod != null) {
      return prod.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
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
    Element val = dom.selectFirst("div.productDeliveryAndReturns_message");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	Elements specsEl = dom.select("div.productDescription_contentWrapper");
    if (specsEl != null && specsEl.size() > 0) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
        Element key = spec.selectFirst("div.productDescription_contentPropertyName span");
        Element value = spec.selectFirst("div.productDescription_contentPropertyValue");

        String strKey = null;
        String strValue = null;

        if (key != null) {
          strKey = key.text().replaceAll(":", "");
        }
        if (value != null)
          strValue = value.text();

        specs.add(new LinkSpec(strKey, strValue));
      }
    }
    return specs;
  }

}
