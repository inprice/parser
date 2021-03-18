package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Lidl Global
 *
 * Contains standard data, all is extracted from html body and via json data in
 * getJsonData()
 *
 * @author mdpinar
 */
public class Lidl extends AbstractWebsite {

	private Document dom;
	private JSONObject json;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

		String prodData = findAPart(html, "var dynamic_tm_data = ", "};", 1);
    if (prodData != null) json = new JSONObject(prodData);
	}

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("product_instock")) {
      return json.getInt("product_instock") > 0;
    }
    return true;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("productid")) {
      return json.getString("productid");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("productname")) {
      return json.getString("productname");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("amount")) {
      return new BigDecimal(cleanDigits(json.getString("amount")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("productbrand")) {
      return json.getString("productbrand");
    }

    Element brand = dom.selectFirst("div.brand div.brand__claim");
    if (brand != null && !brand.text().isEmpty()) {
      return brand.text();
    }

    String[] nameChunks = getName().split("\\s");
    if (nameChunks.length > 1) {
      return nameChunks[0];
    }

    return "Lidl";
  }

  @Override
  public String getSeller() {
    return "Lidl";
  }

  @Override
  public String getShipment() {
    Element shipment = dom.selectFirst("div.delivery span");
    if (shipment != null) {
      return shipment.text();
    }
    return "In-store pickup";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specs = dom.select("div.product-detail-hero li");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String value = spec.text();
        specList.add(new LinkSpec("", value));
      }

      return specList;
    }

    specs = dom.select("div.attributebox__keyfacts li");
    if (specs == null || specs.size() == 0)
      specs = dom.select("div#detail-tab-0 li");

    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String strSpec = spec.text();
        String key = "";
        String value = strSpec;

        if (strSpec.contains(":")) {
          String[] specChunks = strSpec.split(":");
          if (specChunks.length > 1) {
            key = specChunks[0];
            value = specChunks[1];
          }
        }
        specList.add(new LinkSpec(key, value));
      }
    }

    if (specs == null || specs.size() == 0) specList = getValueOnlySpecList(dom.select("div#detailtabProductDescriptionTab li"));
    if (specs == null || specs.size() == 0) specList = getValueOnlySpecList(dom.select("article.textbody li"));
    
    if (specList != null && specList.size() > 0) {
    	for (int i = 0; i < specList.size(); i++) {
				LinkSpec spec = specList.get(i);
				if (StringUtils.isBlank(spec.getKey()) && spec.getValue().indexOf(":") > 0) {
					String[] pair = spec.getValue().split(":");
					spec.setKey(pair[0]);
					spec.setValue(pair[1]);
				}
			}
    }
    
    return specList;
  }

}
