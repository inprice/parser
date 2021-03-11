package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for MediaMarkt Deutschland
 *
 * The parsing steps:
 *
 * - the html body of link's url contains data (in json format) we need 
 * - in getJsonData(), we get that json data by using substring() method of String class 
 * - this data is named as product which is hold on a class-level variable
 * - each data (except for availability and specList) can be gathered using product variable
 *
 * @author mdpinar
 */
public class MediaMarktDE extends AbstractWebsite {

	private JSONObject json;
  private JSONObject article;

  @Override
  protected void getJsonData() {
    final String prodData = findAPart(doc.html(), "__PRELOADED_STATE__ = ", "};", 1);

    if (prodData != null) {
      JSONObject data = new JSONObject(prodData);
      if (data.has("reduxInitialStore")) {
        JSONObject store = data.getJSONObject("reduxInitialStore");
        if (store.has("select")) {
        	json = store.getJSONObject("select");
          if (json.has("article")) {
            article = json.getJSONObject("article");
          }
        }
      }
    }
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("div[data-test='mms-delivery-online-availability']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return true;
    }

    if (json != null && json.has("availability")) {
      JSONObject availability = json.getJSONObject("availability");
      if (availability.has("online")) {
        JSONObject online = availability.getJSONObject("online");
        if (online.has("quantity") && !online.isNull("quantity")) {
          return online.getInt("quantity") > 0;
        }
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("link[itemProp='url']");
    if (val != null && StringUtils.isNotBlank(val.attr("href"))) {
      String[] urlChunks = val.attr("href").split("-");
      if (urlChunks.length > 0) {
        String sku = urlChunks[urlChunks.length-1].replaceAll("[^\\d]", "");
        if (StringUtils.isNotBlank(sku)) return sku;
      }
    }

    if (article != null && article.has("articleId")) {
      return article.getString("articleId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("h1[itemProp='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    if (article != null && article.has("title")) {
      return article.getString("title");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[itemProp='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    if (json != null && json.has("price")) {
      JSONObject price = json.getJSONObject("price");
      if (price.has("price")) {
        return price.getBigDecimal("price");
      }
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Media Markt";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div[data-test='mms-delivery-online-availability']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    if (json != null && json.has("availability")) {
      JSONObject availability = json.getJSONObject("availability");
      if (availability.has("online")) {
        JSONObject online = availability.getJSONObject("online");
        if (online.has("shipping")) {
          JSONObject shipping = online.getJSONObject("shipping");
          if (shipping.has("shippingCosts")) {
            BigDecimal cost = shipping.getBigDecimal("shippingCosts");
            if (cost.compareTo(BigDecimal.ZERO) == 0)
              return "Kostenloser Versand";
            else
              return "Versandkosten " + shipping.getBigDecimal("shippingCosts");
          }
        }
      }
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (article != null && article.has("manufacturer")) {
      return article.getString("manufacturer");
    }

    Element val = doc.selectFirst("meta[itemProp='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = getKeyValueSpecList(doc.select("tr[class^=TableRow__]"), "td:nth-child(1)", "td:nth-child(2)");
    if (specList != null && specList.size() > 0) return specList;

    if (article != null && article.has("mainFeatures")) {
      specList = new ArrayList<>();
      JSONArray features = article.getJSONArray("mainFeatures");
      if (features.length() > 0) {
        for (int i = 0; i < features.length(); i++) {
          JSONObject pair = features.getJSONObject(i);
          specList.add(new LinkSpec(pair.getString("name"), pair.getString("value")));
        }
      }
    }

    return specList;
  }

}
