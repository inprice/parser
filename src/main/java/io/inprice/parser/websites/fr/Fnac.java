package io.inprice.parser.websites.fr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Fnac France
 *
 * Contains standard data, all is extracted from html body and via json data in
 * getJsonData()
 *
 * @author mdpinar
 */
public class Fnac extends AbstractWebsite {

	private Document dom;

	protected JSONObject json;
  protected JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

		Element dataEL = dom.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
    	json = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
      if (json.has("offers")) {
        offers = json.getJSONObject("offers");
      }
    }
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
    if (json != null) {
      if (json.has("sku"))
        return json.getString("sku");
      if (json.has("prid"))
        return "" + json.getInt("prid");
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
  public String getSeller() {
    if (offers != null && offers.has("seller")) {
      JSONObject seller = offers.getJSONObject("seller");
      if (seller.has("name")) {
        return seller.getString("name");
      }
    }
    return "FNAC.COM";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("p.f-buyBox-shipping");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst("div.f-productSpecialsOffers-offerParagraphWrapper");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specs = dom.select("table.f-productDetails-table tr");
    if (specs != null) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        Elements pairs = spec.select("td.f-productDetails-cell");
        if (pairs.size() == 1) {
          specList.add(new LinkSpec("", pairs.get(0).text()));
        } else if (pairs.size() > 1) {
          specList.add(new LinkSpec(pairs.get(0).text(), pairs.get(1).text()));
        }
      }
    }

    return specList;
  }

}
