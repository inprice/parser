package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
 * Parser for Currys UK
 *
 * Contains standard data, all is extracted via json data - product set in
 * getJsonData()
 *
 * @author mdpinar
 */
public class Currys extends AbstractWebsite {

	private Document dom;
  private JSONObject json;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    Element dataEL = dom.getElementById("app.digitalData");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
      if (data.has("product") && !data.getJSONArray("product").isEmpty()) {
        json = data.getJSONArray("product").getJSONObject(0);
      }
    }
	}

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("stockStatus")) {
      String status = json.getString("stockStatus");
      return "In stock".equalsIgnoreCase(status);
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("productSKU")) {
      return json.getString("productSKU");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("productName")) {
      return json.getString("productName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("currentPrice")) {
      return json.getBigDecimal("currentPrice");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("manufacturer")) {
      return json.getString("manufacturer");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.getElementById("delivery");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll("More info", "");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	Elements specsEl = dom.select("div.product-highlight li");
  	if (specsEl != null && specsEl.size() > 0) {
  		specs = new HashSet<>(specsEl.size());
  		for (int i = 0; i < specsEl.size(); i++) {
				Element spec = specsEl.get(i);
				if (spec.text().contains(":")) {
					String[] pair = spec.text().split(":");
					specs.add(new LinkSpec(pair[0], pair[1]));
				} else {
					specs.add(new LinkSpec("", spec.text()));
				}
			}
  	}
  	
  	return specs;
  }

}
