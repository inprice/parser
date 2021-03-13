package io.inprice.parser.websites.nl;

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
 * Parser for Bol the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Bol extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

    Elements scripts = dom.select("script[type='application/ld+json']");
    if (scripts != null) {
      Element dataEL = null;
      for (Element script : scripts) {
        if (script.html().contains("itemCondition")) {
          dataEL = script;
          break;
        }
      }
      if (dataEL != null) {
        json = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
        if (json.has("offers")) {
          offers = json.getJSONObject("offers");
        }
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
      return offers.getString("availability").contains("InStock");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("productID")) {
      return json.getString("productID");
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
    if (offers != null && offers.has("price")) {
      return new BigDecimal(cleanDigits(offers.getString("price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      JSONObject brand = json.getJSONObject("brand");
      if (brand.has("name")) {
        return brand.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    if (offers != null && offers.has("seller")) {
      JSONObject seller = offers.getJSONObject("seller");
      if (seller.has("name")) {
        return seller.getString("name");
      }
    }
    return "bol.com";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("ul.buy-block__usps.check-list--succes.check-list--usps li");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Bekijk alle bezorgopties";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specs = dom.select("dl.specs__list");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {

        Elements titles = spec.select("dt.specs__title");
        Elements values = spec.select("dd.specs__value");

        if (titles.size() > 0 && titles.size() == values.size()) {
          for (int i = 0; i < titles.size(); i++) {
            Element key = titles.get(i);
            Element value = values.get(i);
            specList.add(new LinkSpec(key.text(), value.text()));
          }
        }
      }
    }

    return specList;
  }

}
