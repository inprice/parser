package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Ebay Global
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * finding by item-id : https://www.ebay.com/itm/372661939240
 *
 * @author mdpinar
 */
public class Ebay extends AbstractWebsite {

	private Document dom;
	
  private String brand;
  private List<LinkSpec> specList;

	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);
		buildSpecList();
	}

	@Override
	protected String getHtml() {
		return dom.html();
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.getElementById("vi-quantity__select-box");
    if (val != null) return true;

    val = dom.selectFirst("#qtySubTxt span");
    if (val != null) return true;

    val = dom.selectFirst("a[data-action-name='BUY_IT_NOW']");
    if (val != null) return true;

    val = dom.selectFirst("span[itemprop='availableAtOrFrom']");
    return (val != null);
  }

  @Override
  public String getSku() {
    Element val = dom.getElementById("descItemNumber");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("a[data-itemid]");
    if (val != null && StringUtils.isNotBlank(val.attr("data-itemid"))) {
      return val.attr("data-itemid");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("span#vi-lkhdr-itmTitl");
    if (val == null) val = dom.selectFirst("title");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("h1.product-title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("a[data-itemid]");
    if (val != null && StringUtils.isNotBlank(val.attr("etafsharetitle"))) {
      return val.attr("etafsharetitle");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    String strPrice = null;

    Element val = dom.getElementById("convbinPrice");
    if (val == null) val = dom.getElementById("convbidPrice");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      strPrice = val.text();
    } else {
      val = dom.getElementById("prcIsum");
      if (val == null) val = dom.getElementById("prcIsum_bidPrice");

      if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
        strPrice = val.attr("content");
      } else {
        val = dom.getElementById("mm-saleDscPrc");
        if (val != null && StringUtils.isNotBlank(val.text())) {
          strPrice = val.text();
        }
      }

      if (val == null) {
        val = dom.selectFirst("div.price");
        if (val != null && StringUtils.isNotBlank(val.text())) {
          strPrice = val.text();
        }
      }
    }

    if (strPrice == null)
      return BigDecimal.ZERO;
    else
      return new BigDecimal(cleanDigits(strPrice));
  }

  @Override
  public String getBrand() {
    return brand;
  }

  @Override
  public String getSeller() {
    Element val = dom.getElementById("mbgLink");

    if (val != null && StringUtils.isNotBlank(val.attr("aria-label"))) {
      String[] sellerChunks = val.attr("aria-label").split(":");
      if (sellerChunks.length > 1) {
        return sellerChunks[1];
      }
    } else {
      val = dom.selectFirst("div.seller-persona a");
      if (val == null) val = dom.selectFirst("span.mbg-nw");

      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.getElementById("fshippingCost");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      String left = val.text();
      String right = "";

      val = dom.getElementById("fShippingSvc");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        right = val.text();
      }
      return left + " " + right;
    }

    val = dom.selectFirst("#shSummary span");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst("span.logistics-cost");

    if (val != null && StringUtils.isNotBlank(val.text()) && !"|".equals(val.text().trim())) {
      return val.text();
    } else {
      val = dom.getElementById("shSummary");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return specList;
  }

  private final String BRAND_WORDS = "(Brand|Marca|Marke|Marque).";

  private void buildSpecList() {
  	brand = Consts.Words.NOT_AVAILABLE;

    Elements specs = dom.select("table[role='presentation']:not(#itmSellerDesc) tr");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element row : specs) {
        Elements tds = row.select("td");
        if (tds != null && tds.size() > 0) {
          String key = "";
          String value;
          for (int i = 0; i < tds.size(); i++) {
            if (i % 2 == 0) {
              key = tds.get(i).text();
            } else {
              value = tds.get(i).text();
              specList.add(new LinkSpec(key, value));
              if (key.matches(BRAND_WORDS)) {
                brand = value;
              }
              key = "";
              value = "";
            }
          }
        }
      }
    } else {
      specs = dom.select("#ProductDetails li div");
      if (specs != null && specs.size() > 0) {
        specList = new ArrayList<>();
        for (int i = 0; i < specs.size(); i++) {
          String key = specs.get(i).text();
          String value = "";
          if (i < specs.size() - 1) {
            value = specs.get(++i).text();
          }
          if (key.matches(BRAND_WORDS)) {
            brand = value;
          }
          specList.add(new LinkSpec(key, value));
        }
      }
    }
  }

}
