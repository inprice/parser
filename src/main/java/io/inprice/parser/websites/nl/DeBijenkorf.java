package io.inprice.parser.websites.nl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for DeBijenkorf the Netherlands (protected by cloudflare!!!)
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class DeBijenkorf extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject prod;
  
  @Override
	protected Renderer getRenderer() {
		return Renderer.HEADLESS;
	}

	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

    String prodData = findAPart(html, "Data.product =", "};", 1);

    if (prodData != null) {
      JSONObject data = new JSONObject(prodData);
      if (data.has("product")) {
      	json = data.getJSONObject("product");
        if (json.has("currentVariantProduct")) {
          prod = json.getJSONObject("currentVariantProduct");
        }
      }
    }
	}

  @Override
  public boolean isAvailable() {
    if (prod != null && prod.has("availability")) {
      JSONObject availability = prod.getJSONObject("availability");
      return availability.has("available") && availability.getBoolean("available");
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
    if (json != null && json.has("displayName")) {
      return json.getString("displayName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (prod != null && prod.has("sellingPrice")) {
      JSONObject sellingPrice = prod.getJSONObject("sellingPrice");
      return sellingPrice.getBigDecimal("value");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      JSONObject brand = json.getJSONObject("brand");
      if (brand.has("name")) {
        return brand.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("a[href='#dbk-ocp-delivery-info']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    if (json != null && json.has("groupedAttributes")) {
      JSONArray groupedAttributes = json.getJSONArray("groupedAttributes");
      if (!groupedAttributes.isEmpty()) {
        specs = new HashSet<>();
        for (int i = 0; i < groupedAttributes.length(); i++) {
          JSONObject gattr = groupedAttributes.getJSONObject(i);
          if (!gattr.isEmpty() && gattr.has("attributes")) {
            JSONArray attributes = gattr.getJSONArray("attributes");
            for (int j = 0; j < attributes.length(); j++) {
              JSONObject attr = attributes.getJSONObject(j);
              if (attr.has("label") && attr.has("value")) {
                specs.add(new LinkSpec(attr.getString("label"), attr.getString("value")));
              }
            }
          }
        }
      }
    }

    return specs;
  }

}
