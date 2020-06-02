package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for BestBuy USA
 *
 * The parsing steps:
 *
 * - the html body of competitor's url contains data (in json format) we need 
 * - in getJsonData(), we get that json data by using substring() method of String class 
 * - this data is named as product which is hold on a class-level variable
 * - each data (except for availability and specList) can be gathered using product variable
 *
 * @author mdpinar
 */
public class BestBuy extends AbstractWebsite {

  /*
   * the main data provider derived from json placed in html
   */
  private JSONObject offers;

  public BestBuy(Competitor competitor) {
    super(competitor);
  }

  /**
   * The data we looking for is in html body. So, we get it by using String
   * manipulations
   */
  @Override
  public JSONObject getJsonData() {
    Element dataEL = doc.select("div[data-reactroot] script[type='application/ld+json']").last();
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
    if (offers != null && offers.has("price")) {
      return new BigDecimal(cleanDigits(offers.getString("price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (offers != null && !offers.isNull("seller")) {
      JSONObject seller = offers.getJSONObject("seller");
      if (seller != null) {
        return seller.getString("name");
      }
    }
    return "Best Buy";
  }

  @Override
  public String getShipment() {
    return "Sold and shipped by " + getSeller();
  }

  @Override
  public String getBrand() {
    if (json != null && !json.isNull("brand")) {
      JSONObject brand = json.getJSONObject("brand");
      if (brand != null) {
        return brand.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    if (json != null && !json.isNull("description")) {
      String desc = json.getString("description");
      if (StringUtils.isNotBlank(desc)) {
        specList = new ArrayList<>();

        String[] descChunks = desc.split(";");
        if (descChunks.length > 0) {
          for (String dsc : descChunks) {
            specList.add(new CompetitorSpec("", dsc));
          }
        }
      }

    }

    return specList;
  }
}
