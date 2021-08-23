package io.inprice.parser.websites.xx;

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

import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Apple, Global
 * 
 * https://www.apple.com
 *
 * @author mdpinar
 */
public class AppleXX extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
  
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
  	Element val = dom.selectFirst(".as-purchaseinfo-dudeinfo-deliverymsg, .rf-dude-quote-delivery-message");
    if (val != null && val.text().toLowerCase().contains("in stock")) return true;
    
    val = dom.selectFirst("button[name='add-to-cart']");
    return (val != null);
  }

  @Override
  public String getSku(String url) {
    if (offers != null && offers.has("sku")) {
      return offers.getString("sku");
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
    if (offers != null && offers.has("price")) {
    	return offers.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    return "Apple";
  }

  @Override
  public String getShipment() {
  	Element val = dom.selectFirst(".as-purchaseinfo-dudeinfo");
  	if (val == null) val = dom.selectFirst(".as-purchaseinfo-dudeinfo-deliverypromo");
  	if (val == null) val = dom.selectFirst(".rf-dude-quote-delivery-promo");
    if (val != null) {
    	return val.text();
    }
    return "Check shipping conditions";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getValueOnlySpecs(dom.select(".as-productsummary-addons li"));

  	if (specs == null) specs = getValueOnlySpecs(dom.select(".para-list"));
  	if (specs == null) specs = getValueOnlySpecs(dom.select(".rf-flagship-collapsable-header-text"));
  	
  	return specs;
  }

}
