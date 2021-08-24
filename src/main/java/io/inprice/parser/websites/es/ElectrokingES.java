package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Electroking, Spain
 *
 * https://www.electroking.es
 *
 * @author mdpinar
 */
public class ElectrokingES extends AbstractWebsite {

	private Document dom;

	private JSONObject prod;
	private JSONObject offer;
	private JSONObject shipping;
	
	@Override
	protected ParseStatus setHtml(String html) {
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
      		return ParseStatus.PS_OK;
        }
      }
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	if (offer != null && offer.has("availability")) {
      String availability = offer.getString("availability").toLowerCase();
      return availability.contains("instock") || availability.contains("preorder");
  	}
    return false;
  }

  @Override
  public String getSku(String url) {
  	if (prod != null && prod.has("sku")) {
  		return prod.getString("sku");
  	}
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
    return "Electroking Espania";
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
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	Elements keys = dom.select("dt.name");
  	if (CollectionUtils.isNotEmpty(keys)) {
  		Elements vals = dom.select("dd.value");
  		if (vals != null && vals.size() == keys.size()) {
  			specs = new HashSet<>(keys.size());
  			for (int i = 0; i < keys.size(); i++) {
  				Element key = keys.get(i);
					Element val = vals.get(i);
					specs.add(new LinkSpec(key.text(), val.text()));
				}
  		}
  	}

  	return specs;
  }

}
