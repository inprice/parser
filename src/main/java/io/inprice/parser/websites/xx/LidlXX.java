package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Lidl, Global
 *
 * URL depends on the country
 * UK --> https://www.lidl.co.uk
 * ES --> https://www.lidl.es
 * NL --> https://www.lidl.nl
 *
 * @author mdpinar
 */
public class LidlXX extends AbstractWebsite {

	private Document dom;
	private JSONObject json;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		String prodData = findAPart(html, "var dynamic_tm_data = ", "};", 1);
    if (prodData != null) {
    	json = new JSONObject(prodData);
    	return OK_Status();
    }
    return ParseStatus.PS_NOT_FOUND;
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
  public String getShipment() {
    Element shipment = dom.selectFirst("div.delivery span");
    if (shipment != null) {
      return shipment.text();
    }
    return "In-store pickup";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specsEl = dom.select("div.product-detail-hero li");
    if (CollectionUtils.isNotEmpty(specsEl)) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
        String value = spec.text();
        specs.add(new LinkSpec("", value));
      }

      return specs;
    }

    specsEl = dom.select("div.attributebox__keyfacts li");
    if (specsEl == null || specsEl.size() == 0)
      specsEl = dom.select("div#detail-tab-0 li");

    if (CollectionUtils.isNotEmpty(specsEl)) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
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
        specs.add(new LinkSpec(key, value));
      }
    }

    if (specsEl == null || specsEl.size() == 0) specs = getValueOnlySpecs(dom.select("div#detailtabProductDescriptionTab li"));
    if (specsEl == null || specsEl.size() == 0) specs = getValueOnlySpecs(dom.select("article.textbody li"));
    
    if (CollectionUtils.isNotEmpty(specs)) {
    	for (LinkSpec spec: specs) {
				if (StringUtils.isBlank(spec.getKey()) && spec.getValue().indexOf(":") > 0) {
					String[] pair = spec.getValue().split(":");
					spec.setKey(pair[0]);
					spec.setValue(pair[1]);
				}
			}
    }
    
    return specs;
  }

}
