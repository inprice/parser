package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Laredoute France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Laredoute extends AbstractWebsite {

  private JSONObject offers;

  public Laredoute(Link link) {
    super(link);
  }

  @Override
  protected JSONObject getJsonData() {
    Element dataEL = doc.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
      if (data.has("offers")) {
        offers = data.getJSONObject("offers");

        try {
          JSONArray jarray = offers.getJSONArray("offers");
          offers = jarray.getJSONObject(0);
        } catch (Exception e) {
          //
        }
      }
      return data;
    }
    return super.getJsonData();
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
    Element val = doc.getElementById("vendorsList");
    if (val != null && StringUtils.isNotBlank(val.attr("data-prodid"))) {
      return val.attr("data-prodid");
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
    final String price = findAPart(doc.html(), "\"SalePriceAfterWithCharges\":", ",");

    if (price != null) {
      return new BigDecimal(cleanDigits(price));
    }

    if (offers != null && offers.has("price")) {
      return offers.getBigDecimal("price");
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
    return "Laredoute";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("li.delivery-info-item.delivery-info.delivery-info-content");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
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
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;
    Element specs = doc.getElementsByTag("dscpdp").first();
    if (specs != null) {
      String[] specChunks;
      if (specs.text().indexOf("•") > 0)
        specChunks = specs.text().split("•");
      else
        specChunks = specs.text().split("\\.");

      if (specChunks.length > 0) {
        specList = new ArrayList<>(specChunks.length);
        for (String spec : specChunks) {
          specList.add(new LinkSpec("", spec));
        }
      }
    }
    return specList;
  }
}
