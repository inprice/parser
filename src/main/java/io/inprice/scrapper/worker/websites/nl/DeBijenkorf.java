package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for DeBijenkorf the Netherlands
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class DeBijenkorf extends AbstractWebsite {

  private JSONObject variant;

  public DeBijenkorf(Competitor competitor) {
    super(competitor);
  }

  @Override
  protected JSONObject getJsonData() {
    final String prodData = findAPart(doc.html(), "Data.product =", "};", 1);

    if (prodData != null) {
      JSONObject data = new JSONObject(prodData);
      if (data.has("product")) {
        JSONObject product = data.getJSONObject("product");
        if (product.has("currentVariantProduct")) {
          variant = product.getJSONObject("currentVariantProduct");
        }
        return product;
      }
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (variant != null && variant.has("availability")) {
      JSONObject availability = variant.getJSONObject("availability");
      return availability.has("available") && availability.getBoolean("available");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("code")) {
      return json.getString("code");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("displayName")) {
      return json.getString("displayName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (variant != null && variant.has("sellingPrice")) {
      JSONObject sellingPrice = variant.getJSONObject("sellingPrice");
      return sellingPrice.getBigDecimal("value");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Elements sellers = doc.select("a.dbk-breadcrumb--competitor");
    if (sellers != null && sellers.size() > 0) {
      for (Element seller : sellers) {
        if (seller.attr("href").length() > 1) {
          String halfPure = seller.attr("href").split("/")[1];
          return halfPure.split("#")[0];
        }
      }
    }

    Element seller = doc.select("ul.dbk-breadcrumb-group li.dbk-breadcrumb span").last();
    if (seller != null) {
      return seller.text();
    }
    return "DeBijenkorf";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("a[href='#dbk-ocp-delivery-info']");
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
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    if (json != null && json.has("groupedAttributes")) {
      JSONArray groupedAttributes = json.getJSONArray("groupedAttributes");
      if (!groupedAttributes.isEmpty()) {
        specList = new ArrayList<>();
        for (int i = 0; i < groupedAttributes.length(); i++) {
          JSONObject gattr = groupedAttributes.getJSONObject(i);
          if (!gattr.isEmpty() && gattr.has("attributes")) {
            JSONArray attributes = gattr.getJSONArray("attributes");
            for (int j = 0; j < attributes.length(); j++) {
              JSONObject attr = attributes.getJSONObject(j);
              if (attr.has("label") && attr.has("value")) {
                specList.add(new CompetitorSpec(attr.getString("label"), attr.getString("value")));
              }
            }
          }
        }
      }
    }

    return specList;
  }
}
