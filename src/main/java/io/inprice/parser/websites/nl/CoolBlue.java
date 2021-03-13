package io.inprice.parser.websites.nl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for CoolBlue the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CoolBlue extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

    Element dataEL = dom.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
    	json = new JSONObject(dataEL.dataNodes().get(0).getWholeData().replace("\r\n", " "));
      if (json.has("offers")) {
        offers = json.getJSONObject("offers");
      }
    }
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      String availability = offers.getString("availability");
      return availability.contains("InStock") || availability.contains("PreOrder");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (isAvailable()) {
      if (offers != null && offers.has("price")) {
        return offers.getBigDecimal("price");
      }
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
  public String getSeller() {
    return "CoolBlue";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("span.js-delivery-information-usp");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(dom.select("div.product-specs__list-item.js-product-specs--list-item"),
        ".product-specs__item-title", ".product-specs__item-spec");
  }

}
