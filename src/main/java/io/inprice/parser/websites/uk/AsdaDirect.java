package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Asda Direct UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class AsdaDirect extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

  @Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}
  
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (dataEL != null && dataEL.size() > 0) {
    	for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
        if (data.has("@type")) {
          String type = data.getString("@type");
          if (type.equals("Product")) {
          	json = data;
            if (json.has("offers")) {
          		offers = json.getJSONObject("offers");
          		return HttpStatus.OK;
            }
          }
        }
      }
    }
		return HttpStatus.NOT_FOUND;
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
  public String getSku() {
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
    if (offers != null) {
    	String price = null;
    	if (offers.has("highPrice")) price = offers.getString("highPrice");
    	if (offers.has("price")) price = offers.getString("price");
    	if (offers.has("lowPrice")) price = offers.getString("lowPrice");
      
    	if (price != null) {
    		return new BigDecimal(cleanDigits(price));
    	}
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
    return "Check delivery info";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getValueOnlySpecs(dom.select(".product-description li"));
  	if (specs != null) return specs;
  	
		Element desc = dom.selectFirst("meta[name='description']");
		if (desc != null) {
			specs = new HashSet<>();
			String[] chunks = desc.attr("content").split("\n");
			for (int i = 0; i < chunks.length; i++) {
				specs.add(new LinkSpec("", chunks[i].replaceAll("•", "")));
			}
		}
  	return specs;
  }

}
