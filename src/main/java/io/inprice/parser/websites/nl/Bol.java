package io.inprice.parser.websites.nl;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Bol the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Bol extends AbstractWebsite {

  /*
   * holds price info set in getJsonData()
   */
  private JSONObject offers;

  public Bol(Link link) {
    super(link);
  }

  @Override
  public JSONObject getJsonData() {
    Elements scripts = doc.select("script[type='application/ld+json']");
    if (scripts != null) {
      Element dataEL = null;
      for (Element script : scripts) {
        if (script.html().contains("itemCondition")) {
          dataEL = script;
          break;
        }
      }
      if (dataEL != null) {
        JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
        if (data.has("offers")) {
          if (data.has("offers")) {
            offers = data.getJSONObject("offers");
          }
        }
        return data;
      }
    }
    return super.getJsonData();
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
  public String getShipment() {
    Element val = doc.selectFirst("ul.buy-block__usps.check-list--succes.check-list--usps li");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Bekijk alle bezorgopties";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specs = doc.select("dl.specs__list");
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
