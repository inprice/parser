package io.inprice.parser.websites.de;

import java.math.BigDecimal;
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
 * NotebooksBilliger, Germany
 *
 * https://www.notebooksbilliger.de
 *
 * @author mdpinar
 */
public class NotebooksBilligerDE extends AbstractWebsite {

	private Document dom;
	private JSONObject prod;
	
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (dataEL != null) {
      for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
        if (data.has("@type") && data.getString("@type").equals("Product")) {
          prod = data;
          return HttpStatus.OK;
        }
      }
    }
    return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	if (prod != null && prod.has("offers")) {
  		JSONObject offers = prod.getJSONObject("offers");
      if (offers != null && offers.has("availability")) {
        String availability = offers.getString("availability").toLowerCase();
        return availability.contains("instock") || availability.contains("preorder");
      }
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
  	if (prod != null && prod.has("offers")) {
  		JSONObject offer = prod.getJSONObject("offers");
  		if (!offer.isEmpty()) {
  			String price = offer.getString("price");
  			return new BigDecimal(cleanDigits(price));
  		}
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (prod != null && prod.has("offers")) {
  		JSONObject brand = prod.getJSONObject("brand");
  		if (!brand.isEmpty()) {
  			return brand.getString("name");
  		}
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst(".product-price__info-wrapper > .product-price__info");
    if (val != null) {
      return val.text();
    }
    return "Abholung im Geschäft";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("table.properties_table tr"), "td.produktDetails_eigenschaft2", "td.produktDetails_eigenschaft3");
  }

}
