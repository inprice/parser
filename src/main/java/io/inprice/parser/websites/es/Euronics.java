package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Euronics Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Euronics extends AbstractWebsite {

	private Document dom;
	
	private JSONObject prod;
	private JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (dataEL != null) {
      for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
        if (data.has("@type") && data.getString("@type").equals("Product")) {
          prod = data;
          if (prod.has("offers")) {
          	offers = prod.getJSONObject("offers");
          }
          break;
        }
      }
    }
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
    	return (offers.getString("availability").indexOf("InStock") > -1);
    }
    return false;
  }

  @Override
  public String getSku() {
    if (prod != null && prod.has("sku")) {
    	return prod.getString("sku");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (prod != null && prod.has("name")) {
    	return prod.getString("name");
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
    if (prod != null && prod.has("brand")) {
    	JSONObject brand = prod.getJSONObject("brand");
    	if (brand != null && brand.has("name")) {
    		return brand.getString("name");
    	}
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
  	String shipping = "";

  	Element info = dom.selectFirst(".link.active .shippingType-info");
  	Element text = dom.selectFirst(".link.active .shippingType-text");
  	
  	if (info != null) shipping += info.text();
  	if (text != null) shipping += " " + text.text();
  	
    return (StringUtils.isBlank(shipping) ? Consts.Words.NOT_AVAILABLE : shipping);
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("div#more_product_info li"), "span:nth-child(1)", "span:nth-child(2)");
  }

}
