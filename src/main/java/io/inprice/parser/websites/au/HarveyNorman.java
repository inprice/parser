package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.browser.BrowserManager;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Pair;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Please note that: This site is guarded by incapsula that is a scrapper
 * blocking service and checks every request for a set of cookies starting with
 * visid_incap_ and incap_ses_ For this reason, we have to add those cookies in
 * header (please refer to getHeader() method)
 *
 * Parser for HarveyNorman Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class HarveyNorman extends AbstractWebsite {

  /*
   * holds price info set in getJsonData()
   */
  private JSONObject offer;

  @Override
  protected void openPage() {
    String problem = null;
    try {

      Pair response = BrowserManager.getHarveyNormanBrowser().getHtml(getUrl());

      if (response.getStatus() < 400) {
        doc = Jsoup.parse(response.getBody());
      } else {
        problem = "Access problem to HarveyNorman";
      }

    } catch (Exception e) {
      log.error(getUrl(), e);
      problem = e.getMessage();
    }

    if (problem != null) {
      setLinkStatus(problem);
    }
  }

  @Override
  public JSONObject getJsonData() {
    Elements dataEL = doc.select("script[type='application/ld+json']");
    if (dataEL != null && dataEL.size() > 0) {
      for (int i = 0; i < dataEL.size(); i++) {
        if (dataEL.get(i).dataNodes().get(0).getWholeData().indexOf("priceCurrency") > 0) {
          JSONObject data = new JSONObject(dataEL.get(i).dataNodes().get(0).getWholeData().replace("\r\n", " "));
          if (data.has("offers")) {
            JSONArray offers = data.getJSONArray("offers");
            if (!offers.isEmpty() && offers.length() > 0) {
              offer = offers.getJSONObject(0);
            }
          }
          return data;
        }
      }
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    return true;
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
    if (offer != null && offer.has("price")) {
      return offer.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (offer != null && offer.has("seller")) {
      JSONObject seller = offer.getJSONObject("seller");
      if (seller.has("name")) {
        return seller.getString("name");
      }
    }
    return "Harvey Norman";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getJSONObject("brand").getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element shipment = doc.selectFirst("span.margin-vertical-xsmall.font-weight-light");
    if (shipment != null) {
      return shipment.text();
    }
    return "See delivery details";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-long-description li"));
  }

}
