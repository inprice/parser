package io.inprice.parser.websites.fr;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.StringHelper;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Fnac, France BLOCKED!!!
 *
 * https://www.fnac.com
 *
 * @author mdpinar
 */
public class FnacFR extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
	private JSONObject offers;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		
    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (CollectionUtils.isNotEmpty(dataEL)) {
    	for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelper.escapeJSON(dNode.getWholeData()));
        if (data.has("@type")) {
          String type = data.getString("@type");
          if (type.equals("Product")) {
          	json = data;
            if (json.has("offers")) {
            	Object offersObj = json.get("offers");
            	if (offersObj instanceof JSONObject) {
            		offers = json.getJSONObject("offers");
            	} else {
            		JSONArray offersArr = (JSONArray) offersObj;
            		offers = offersArr.getJSONObject(0);
            	}
          		return OK_Status();
            }
          }
        }
      }
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      String availability = offers.getString("availability").toLowerCase();
      return availability.contains("instock") || availability.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null) {
      if (json.has("sku"))
        return json.getString("sku");
      if (json.has("prid"))
        return "" + json.getInt("prid");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (offers != null && offers.has("price")) {
      return offers.getBigDecimal("price");
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
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    if (offers != null && offers.has("seller")) {
      JSONObject seller = offers.getJSONObject("seller");
      if (seller.has("name")) {
        return seller.getString("name");
      }
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst(".f-buyBox-shipping");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst(".f-productSpecialsOffers-offerParagraphWrapper");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select("div.characteristicsStrate__list dl"), "dt", "dd");
  	if (CollectionUtils.isNotEmpty(specs)) return specs;

    Elements specsEl = dom.select("table.f-productDetails-table tr");
    if (specs != null) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
        Elements pairs = spec.select("td.f-productDetails-cell");
        if (pairs.size() == 1) {
          specs.add(new LinkSpec("", pairs.get(0).text()));
        } else if (pairs.size() > 1) {
          specs.add(new LinkSpec(pairs.get(0).text(), pairs.get(1).text()));
        }
      }
    }

    return specs;
  }

}
