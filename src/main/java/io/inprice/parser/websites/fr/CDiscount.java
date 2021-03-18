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
 * Parser for CDiscount France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CDiscount extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    Elements scripts = dom.select("script[type='application/ld+json']");
    if (scripts != null && StringUtils.isNotBlank(scripts.html())) {
      
    	Element dataEL = null;
      for (Element script : scripts) {
        if (script.html().contains("itemCondition")) {
          dataEL = script;
          break;
        }
      }
      if (dataEL != null) {
      	json = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
        if (json.has("offers")) {
          offers = json.getJSONObject("offers");
        }
      }
    }
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
  public String getSeller() {
    Element seller = dom.selectFirst("a.fpSellerName");
    if (seller == null)
      seller = dom.selectFirst("span.logoCDS");

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
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specs = dom.select("div#fpBulletPointReadMore li");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String[] specChunks = spec.text().split(":");
        LinkSpec ls = new LinkSpec(specChunks[0], "");
        if (specChunks.length > 1) {
          ls.setValue(specChunks[1]);
        }
        specList.add(ls);
      }
    }

    if (specList == null) {
      specs = dom.select("table.fpDescTb tr");
      if (specs != null && specs.size() > 0) {
        specList = new ArrayList<>();
        for (Element spec : specs) {
          Elements pairs = spec.select("td");
          if (pairs.size() > 0) {
            LinkSpec ls = new LinkSpec(pairs.get(0).text(), "");
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
