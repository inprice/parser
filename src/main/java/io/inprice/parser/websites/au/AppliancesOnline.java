package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for AppliancesOnline Australia
 *
 * There is no way to fetch product data over html body.
 * So, we need to make two requests; one for product info and other one is for specifications!
 *
 * @author mdpinar
 */
public class AppliancesOnline extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

	@Override
	protected By waitBy() {
		return By.className("aol-product-price");
	}
	
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		String title = dom.title();
		if (title.toLowerCase().contains("page not found") == false) {
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
  	String[] chunks = getUrl().split("-");
    return chunks[chunks.length-1];
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst(".heading-is-product-title");
    if (val != null) {
    	return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst(".aol-product-price");
    if (val != null) {
      return new BigDecimal(cleanDigits(val.text()));
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
    return "Check delivery and installation info";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select(".specification-item .attribute-row"), ".attribute-name", ".attribute-value");
  }

}
