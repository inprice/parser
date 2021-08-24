package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Zalando, Global
 *
 * URL depends on the country
 * DE --> https://www.zalando.de
 * ES --> https://www.zalando.es
 * FR --> https://www.zalando.fr
 * IT --> https://www.zalando.it
 * NL --> https://www.zalando.nl
 * UK --> https://www.zalando.co.uk
 *
 * @author mdpinar
 */
public class ZalandoXX extends AbstractWebsite {

	private JSONObject info;
	private JSONObject price;
	private JSONArray details;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	protected ParseStatus setHtml(String html) {
		String rawJson = findAPart(html, "![CDATA[{\"layout\"", "}]]", 1, 8);
		if (StringUtils.isNotBlank(rawJson)) {
			JSONObject json = new JSONObject(rawJson);
			if (json != null && json.has("model")) {
				JSONObject model = json.getJSONObject("model");
				if (model != null) {
					if (model.has("articleInfo")) {
						info = model.getJSONObject("articleInfo");
					}
					if (model.has("displayPrice")) {
						price = model.getJSONObject("displayPrice");
					}
					if (model.has("productDetailsCluster")) {
						details = model.getJSONArray("productDetailsCluster");
					}
					return ParseStatus.PS_OK;
				}
			}
		}
		return ParseStatus.PS_NOT_FOUND;
	}
	
  @Override
  public boolean isAvailable() {
  	if (info != null && info.has("available")) {
  		return info.getBoolean("available");
  	}
    return false;
  }

  @Override
  public String getSku(String url) {
  	if (info != null && info.has("id")) {
  		return info.getString("id");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	if (info != null && info.has("name")) {
  		return info.getString("name");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	if (price != null && price.has("price")) {
  		JSONObject _price = price.getJSONObject("price");
  		if (_price == null) _price = price.getJSONObject("originalPrice");
  		if (_price != null && _price.has("value")) {
  			return _price.getBigDecimal("value");
  		}
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (info != null && info.has("brand")) {
  		JSONObject brand = info.getJSONObject("brand");
  		if (brand != null && brand.has("name")) {
  			return brand.getString("name");
  		}
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Standard";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	if (details != null && details.length() > 0) {
  		specs = new HashSet<>();
  		for (int i = 0; i < details.length(); i++) {
				JSONObject attr = details.getJSONObject(i);
				if (attr != null && attr.has("data")) {
					JSONArray data = attr.getJSONArray("data");
					if (data != null && data.length() > 0) {
						for (int j = 0; j < data.length(); j++) {
							JSONObject keyVal = data.getJSONObject(j);
							if (keyVal != null && keyVal.has("name") && keyVal.has("values")) {
								String key = keyVal.getString("name");
								String val = keyVal.getString("values");
								
								if (StringUtils.isNotBlank(key) && key.indexOf("_") > 0) continue;
								if (StringUtils.isNotBlank(key) || StringUtils.isNotBlank(val))
				          specs.add(new LinkSpec(key, val));
							}
						}
					}
				}
			}
  	}

    return specs;
  }
  
}
