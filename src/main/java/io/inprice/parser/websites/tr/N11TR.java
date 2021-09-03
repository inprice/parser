package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * n11, Turkiye
 *
 * https://urun.n11.com
 *
 * @author mdpinar
 */
public class N11TR extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

  @Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
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
    if (offers != null && offers.has("offerCount")) {
    	String offerCount = offers.getString("offerCount");
    	return (NumberUtils.toInt(offerCount) > 0);
    }
    return false;
  }

  @Override
  public String getSku() {
  	Element val = dom.selectFirst("input[name='skuId']");
    if (val != null && val.hasAttr("value")) {
      return val.attr("value");
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
    	if (offers.has("lowPrice")) {
    		return offers.getBigDecimal("lowPrice");
    	} else if (offers.has("price")) {
    		return offers.getBigDecimal("price");
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
  public String getShipment() {
  	Element val = dom.selectFirst(".cargo-price");
    if (val != null) {
      return val.text();
    }
    return "Detayları gör";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select("div.ProductSpecification"), "dd", "dt");
  	if (specs != null) return specs;
  	
  	specs = getKeyValueSpecs(dom.select("div.tab-Specification li"), "div.meta", "div.subMeta");
  	Set<LinkSpec> features = getValueOnlySpecs(dom.select("div.contentList li"), "div.meta");

  	if (specs != null && features == null) return specs;
  	if (features != null && specs == null) return features;

  	if (specs == null) {
  		specs = getValueOnlySpecs(dom.select("div.product-details li"));
  		if (features == null) return specs;
  	}

  	features.addAll(specs);
  	return features;
  }

}
