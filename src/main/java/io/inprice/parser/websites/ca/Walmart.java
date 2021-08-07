package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Walmart CA!
 * 
 * This site can sense us! We must not handle urls concurrently.
 * This site's urls must be handled over singly queue! 
 *
 * @author mdpinar
 */
public class Walmart extends AbstractWebsite {

	private Document dom;
	private String features;
	
	@Override
	protected HttpStatus setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
		
		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().equals("Walmart Canada") == false) {
			String starting = "facets\":";
			String ending = ",\"badges";
			features = findAPart(html, starting, ending, 0, starting.length());
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	Element val = dom.selectFirst("[data-automation='cta-button']");
  	return (val != null && val.hasAttr("disabled") == false);
  }

  @Override
  public String getSku() {
  	String[] chunks = getUrl().split("/");
    return chunks[chunks.length-1];
  }

  @Override
  public String getName() {
  	Element val = dom.selectFirst("[data-automation='product-title']");
  	if (val != null) {
  		return val.text();
  	}
  	return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element val = dom.selectFirst("[data-automation='buybox-price']");
  	if (val != null) {
      return new BigDecimal(cleanDigits(val.text()));
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	Element val = dom.selectFirst("[data-automation='brand'] a");
  	if (val != null) {
  		return val.text();
  	}
  	return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
  	Element val = dom.selectFirst("[data-automation='vendor-link']");
  	if (val != null) {
  		return val.text();
  	}
  	return "Walmart";
  }

  @Override
  public String getShipment() {
  	Element shipment = dom.selectFirst("[data-automation='fulfillment-options-shipping']");
  	if (shipment != null) {
      return shipment.text();
    }

  	shipment = dom.selectFirst("div[data-automation='fulfillment-options-pickup']");
  	if (shipment != null) {
      return shipment.text();
    }

    return "Shipped by " + getSeller();
  }
  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    if (features != null) {
      JSONArray specsArr = new JSONArray(features);
      if (specsArr.length() > 0) {
        specs = new HashSet<>();
        for (int i = 0; i < specsArr.length(); i++) {
					JSONObject specObj = specsArr.getJSONObject(i);
					specs.add(new LinkSpec(specObj.getString("name"), specObj.getString("value")));
        }
      }
    }

    return specs;
  }

}
