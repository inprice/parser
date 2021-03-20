package io.inprice.parser.websites.fr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebRequest;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Laredoute France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Laredoute extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
	
	@Override
	protected void beforeRequest(WebRequest req) {
		req.setAdditionalHeader(HttpHeader.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/*;q=0.8");
    req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
    req.setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, "gzip, deflate, br");
		req.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
	}
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (dataEL != null) {
      for (DataNode dNode : dataEL.dataNodes()) {
        JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
        if (data.has("@type")) {
          String type = data.getString("@type");
          if (type.equals("Product")) {
          	json = data;
          	if (json.has("offers")) {
              try {
              	offers = json.getJSONObject("offers");
              	if (offers == null) {
                  JSONArray arr = offers.getJSONArray("offers");
                  offers = arr.getJSONObject(0);
              	}
              } catch (Exception e) { }
          	}
            break;
          }
        }
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
    return "Laredoute";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("li.delivery-info-item.delivery-info.delivery-info-content");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;
    Element specs = dom.getElementsByTag("dscpdp").first();
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
