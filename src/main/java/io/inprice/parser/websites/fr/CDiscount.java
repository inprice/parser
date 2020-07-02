package io.inprice.parser.websites.fr;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
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
 * Parser for CDiscount France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CDiscount extends AbstractWebsite {

  /*
   * holds price info set in getJsonData()
   */
  private JSONObject offers;

  public CDiscount(Competitor competitor) {
    super(competitor);
  }

  @Override
  public JSONObject getJsonData() {
    Elements scripts = doc.select("script[type='application/ld+json']");
    if (scripts != null && StringUtils.isNotBlank(scripts.html())) {
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
  public String getSeller() {
    Element seller = doc.selectFirst("a.fpSellerName");
    if (seller == null)
      seller = doc.selectFirst("span.logoCDS");

    if (seller != null) {
      return seller.text();
    }
    return "CDiscount";
  }

  @Override
  public String getShipment() {
    return "Vendu et expédié par " + getSeller();
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      JSONObject merchant = json.getJSONObject("brand");
      if (merchant.has("name")) {
        return merchant.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    Elements specs = doc.select("div#fpBulletPointReadMore li");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String[] specChunks = spec.text().split(":");
        CompetitorSpec ls = new CompetitorSpec(specChunks[0], "");
        if (specChunks.length > 1) {
          ls.setValue(specChunks[1]);
        }
        specList.add(ls);
      }
    }

    if (specList == null) {
      specs = doc.select("table.fpDescTb tr");
      if (specs != null && specs.size() > 0) {
        specList = new ArrayList<>();
        for (Element spec : specs) {
          Elements pairs = spec.select("td");
          if (pairs.size() > 0) {
            CompetitorSpec ls = new CompetitorSpec(pairs.get(0).text(), "");
            if (pairs.size() > 1) {
              ls.setValue(pairs.get(1).text());
            }
            specList.add(ls);
          }
        }
      }
    }

    return specList;
  }
}
