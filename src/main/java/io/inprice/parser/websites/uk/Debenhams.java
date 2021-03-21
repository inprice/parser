package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * Parser for Debenhams UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Debenhams extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
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
            }
          }
        }
      }
    }
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[name='twitter:data2']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return "In Stock".equals(val.attr("content"));
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null) {
      if (json.has("sku")) {
        return json.getString("sku");
      }
      if (json.has("@id")) {
        String id = json.getString("@id");
        if (id != null)
          return cleanDigits(id);
      }
    }

    Element sku = dom.selectFirst("span.item-number-value");
    if (sku != null) {
      return sku.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }

    Element val = dom.selectFirst("div#ProductTitle span.title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (isAvailable()) {
      if (offers != null) {
        if (!offers.isEmpty() && offers.has("lowPrice")) {
          return offers.getBigDecimal("lowPrice");
        } else if (!offers.isEmpty() && offers.has("price")) {
          return offers.getBigDecimal("price");
        }
      }
      
      Element val = dom.selectFirst("span.VersionOfferPrice img");
      if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
        return new BigDecimal(cleanDigits(val.attr("alt")));
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null) {
      if (json.has("brand")) {
        return json.getJSONObject("brand").getString("name");
      }
      if (json.has("schema:brand")) {
        return json.getString("schema:brand");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div.pw-dangerous-html.dbh-content");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.getElementById("hd3");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "In-store pickup";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("div.pw-dangerous-html li"));
  }

}
