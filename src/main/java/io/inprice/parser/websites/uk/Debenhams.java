package io.inprice.parser.websites.uk;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Debenhams UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Debenhams extends AbstractWebsite {

  /*
   * holds price info set in getJsonData()
   */
  private JSONObject offers;

  public Debenhams(Link link) {
    super(link);
  }

  @Override
  public JSONObject getJsonData() {
    Element dataEL = doc.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
      if (data.has("offers")) {
        offers = data.getJSONObject("offers");
      }
      return data;
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("meta[name='twitter:data2']");
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

    Element sku = doc.selectFirst("span.item-number-value");
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

    Element val = doc.selectFirst("div#ProductTitle span.title");
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
      
      Element val = doc.selectFirst("span.VersionOfferPrice img");
      if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
        return new BigDecimal(cleanDigits(val.attr("alt")));
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Debenhams";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div.pw-dangerous-html.dbh-content");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.getElementById("hd3");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "In-store pickup";
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
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.pw-dangerous-html li"));
  }

}
