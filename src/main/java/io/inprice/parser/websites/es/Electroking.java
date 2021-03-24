package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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
 * Parser for Electroking Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Electroking extends AbstractWebsite {

	private Document dom;

	private JSONObject prod;
	private JSONObject offer;
	private JSONObject shipping;
	
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
          offer = data.getJSONObject("offers");
          
          String shipRawJson = findAPart(html, "\"shipping\":", "},", 1, 0);
          shipping = new JSONObject(shipRawJson);
          break;
        }
      }
    }
	}

  @Override
  public boolean isAvailable() {
  	if (offer != null && offer.has("availability")) {
  		String availability = offer.getString("availability");
  		return availability.contains("InStock");
  	}
    return false;
  }

  @Override
  public String getSku() {
  	if (offer != null && offer.has("sku")) {
  		return offer.getString("sku");
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
  	if (offer != null && offer.has("price")) {
  		return new BigDecimal(cleanDigits(offer.getString("price")));
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (prod != null && prod.has("brand")) {
  		JSONObject brand = prod.getJSONObject("brand");
  		return brand.getString("name");
  	}
    return getSeller();
  }

  @Override
  public String getShipment() {
  	if (shipping != null) {
  		String value = shipping.getString("value");
  		if (value.equals("Gratis")) {
  			return "Transporte gratis";
  		} else {
  			return "Coste de transporte: " + shipping.getBigDecimal("amount").setScale(2, RoundingMode.HALF_UP);
  		}
  	}
    return "Envío por Agencia de Transporte. Ver detalles";
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	List<LinkSpec> specList = null;

  	Elements keys = dom.select("dt.name");
  	if (keys != null && keys.size() > 0) {
  		Elements vals = dom.select("dd.value");
  		if (vals != null && vals.size() == keys.size()) {
  			specList = new ArrayList<>(keys.size());
  			for (int i = 0; i < keys.size(); i++) {
  				Element key = keys.get(i);
					Element val = vals.get(i);
					specList.add(new LinkSpec(key.text(), val.text()));
				}
  		}
  	}

  	return specList;
  }

}
