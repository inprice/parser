package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Rakuten Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public abstract class Rakuten extends AbstractWebsite {

  private JSONObject offer;

  @Override
  protected JSONObject getJsonData() {
    final String prodData = findAPart(doc.html(), "productDetails =", "};", 1);

    if (prodData != null) {
      return new JSONObject(prodData);
    } else {
      Elements dataELs = doc.select("script[type='application/ld+json']");
      if (dataELs != null && dataELs.size() > 0) {
        for (Element dataEL : dataELs) {
          JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
          if (data.has("@type") && data.getString("@type").equals("Product")) {
            if (data.has("offers")) {
              JSONObject offersParent = data.getJSONObject("offers");
              if (offersParent.has("offers")) {
                JSONArray offers = offersParent.getJSONArray("offers");
                if (offers.length() > 0) {
                  offer = offers.getJSONObject(0);
                }
              } else {
                offer = offersParent;
              }
            }
            return data;
          }
        }
      }
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("isAvailable")) {
      return json.getBoolean("isAvailable");
    }

    if (offer != null && offer.has("availability")) {
      return offer.getString("availability").contains("InStock");
    }

    Element available = doc.selectFirst("meta[property='product:availability']");
    if (available != null) {
      return available.attr("content").trim().contains("in stock");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null) {
      if (json.has("articleNumber")) {
        return json.getString("articleNumber");
      }
      if (json.has("mpn")) {
        return json.getString("mpn");
      }
    }

    Element sku = doc.selectFirst("meta[property='product:retailer_item_id']");
    if (sku != null) {
      return sku.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }

    Element name = doc.selectFirst("meta[property='og:title']");
    if (name != null) {
      return name.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null) {
      if (json.has("price")) {
        return json.getBigDecimal("price");
      }
      if (json.has("offers")) {
        JSONObject offersParent = json.getJSONObject("offers");
        if (offersParent.has("lowPrice")) {
          return offersParent.getBigDecimal("lowPrice");
        } else if (offer.has("price")) {
          return offer.getBigDecimal("price");
        }
      }
    }

    Element price = doc.selectFirst("meta[property='product:price:amount']");
    if (price != null) {
      return new BigDecimal(cleanDigits(price.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (json != null && json.has("details")) {
      JSONObject details = json.getJSONObject("details");
      if (details != null && details.has("merchant")) {
        JSONObject seller = details.getJSONObject("merchant");
        if (seller.has("company")) {
          return seller.getString("company");
        }
      }
    }

    if (offer != null && offer.has("seller")) {
      JSONObject seller = offer.getJSONObject("seller");
      if (seller.has("name")) {
        return seller.getString("name");
      }
    }

    Element val = doc.selectFirst("meta[property='og:description']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return "Rakuten";
  }

  @Override
  public String getShipment() {
    if (json != null && json.has("shipping")) {
      JSONObject shipping = json.getJSONObject("shipping");
      if (shipping.has("price")) {
        BigDecimal shippingPrice = shipping.getBigDecimal("price");
        if (shippingPrice.compareTo(BigDecimal.ZERO) > 0) {
          return "Shipping cost " + shippingPrice;
        } else {
          return "Free shipping";
        }
      }
    }

    Element val = doc.selectFirst("p.freeShipping");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("ul.shipping li.value");
    if (val == null) val = doc.selectFirst("li.shipping_amount span.value");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (json != null) {
      if (json.has("brandName")) {
        return json.getString("brandName");
      }
      if (json.has("brand")) {
        JSONObject brand = json.getJSONObject("brand");
        if (brand.has("name")) {
          return brand.getString("name");
        }
      }
    }

    Element val = doc.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    if (json != null && json.has("details")) {
      JSONObject details = json.getJSONObject("details");
      if (details.has("features")) {
        specList = new ArrayList<>();

        JSONArray features = details.getJSONArray("features");
        for (int i = 0; i < features.length(); i++) {
          JSONObject pair = features.getJSONObject(i);
          specList.add(new LinkSpec(pair.getString("key"), pair.getString("value")));
        }
      }
    }

    if (specList == null || specList.size() == 0) {
      specList = getValueOnlySpecList(doc.select("div.product-description li"));
      if (specList != null && specList.size() > 0)
        return specList;

      return getKeyValueSpecList(doc.select("div.specs_ctn tr"), "th.label", "td.value");
    }

    return specList;
  }

}
