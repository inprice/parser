package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * VidaXL, Global
 *
 * https://www.vidaxl.com
 *
 * @author mdpinar
 */
public class VidaXLXX extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
	private JSONObject offers;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	protected ParseStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (CollectionUtils.isNotEmpty(dataEL)) {
    	for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
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
          		return ParseStatus.PS_OK;
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
    if (json != null) {
      if (json.has("sku"))
        return json.getString("sku");
      if (json.has("prid"))
        return "" + json.getInt("prid");
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
    if (offers != null) {
    	if (offers.has("price")) {
    		return offers.getBigDecimal("price");
    	}
    	if (offers.has("priceSpecification")) {
    		JSONObject pspec = offers.getJSONObject("priceSpecification");
    		if (pspec != null && pspec.has("price")) {
    			return pspec.getBigDecimal("price");
    		}
    	}
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getJSONObject("brand").getString("name");
    }

    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller(String defaultSeller) {
    Element val = dom.selectFirst(".seller-info-name");
    if (val != null) {
      return val.text();
    }
    return defaultSeller;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("ul.product-details li:nth-child(2)");
    if (val == null) val = dom.selectFirst(".row.mb-1");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replace("checkmark", "");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	Elements specsEl = dom.select("div.product-specifications__text li");
  	if (specsEl == null || specsEl.size() == 0) specsEl = dom.select(".value.content li");
  	
  	if (CollectionUtils.isNotEmpty(specsEl)) {
  		specs = new HashSet<>(specsEl.size());
  		for (int i = 0; i < specsEl.size(); i++) {
				Element specEL = specsEl.get(i);
				String[] pair = specEL.text().split(":");
				if (pair.length == 1) {
					specs.add(new LinkSpec("", pair[0]));
				} else {
					specs.add(new LinkSpec(pair[0], pair[1]));
				}
			}
  	}
  	
    return specs;
  }

}
