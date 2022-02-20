package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Ebay, Global
 *
 * https://www.ebay.com
 *
 * @author mdpinar
 */
public class EbayXX extends AbstractWebsite {

	private Document dom;
	
  private String brand;
  private Set<LinkSpec> specs;
  
	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		
		String title = dom.title();
		if (title.toLowerCase().contains("error page") == false) {
			buildSpecList();
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
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
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("span#vi-lkhdr-itmTitl");
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

    return dom.title();
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
    Element val = dom.selectFirst("[itemprop='brand']");
    if (val != null) {
      return val.text();
    }
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
    
    return super.getSeller();
  }

  @Override
  public String getShipment() {
  	String res = Consts.Words.CHECK_DELIVERY_CONDITIONS;

    Element val = dom.getElementById("fshippingCost");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      String left = val.text();
      String right = "";

      val = dom.getElementById("fShippingSvc");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        right = val.text();
      }
      res = left + " " + right;
    }

    if (res.equals(GlobalConsts.NOT_AVAILABLE)) {
      val = dom.selectFirst("#shSummary span");
      if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst("span.logistics-cost");
  
      if (val != null && StringUtils.isNotBlank(val.text()) && !"|".equals(val.text().trim())) {
      	res = val.text();
      } else {
        val = dom.getElementById("shSummary");
        if (val != null && StringUtils.isNotBlank(val.text())) {
        	res = val.text();
        }
      }
    }

    if (!res.equals(GlobalConsts.NOT_AVAILABLE)) {
    	if (res.indexOf("not ship") > 0 && res.indexOf("-") > 0) res = res.split("-")[1];
    }

    return res;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return specs;
  }

  private static final String BRAND_WORDS = "(Brand|Marca|Marke|Marque)?";

  private void buildSpecList() {
    Elements specsEl = dom.select("div.itemAttr table[role='presentation']:not(#itmSellerDesc) tr");
    if (CollectionUtils.isNotEmpty(specsEl)) {
      specs = new HashSet<>();
      for (Element row : specsEl) {
        Elements tds = row.select("td");
        if (CollectionUtils.isNotEmpty(tds)) {
          String key = "";
          String value;
          for (int i = 0; i < tds.size(); i++) {
            if (i % 2 == 0) {
              key = tds.get(i).text().replace(":", "");
            } else {
              value = tds.get(i).text();
              specs.add(new LinkSpec(key, value));
              key = "";
              value = "";
            }
          }
        }
      }
    } else {
      specsEl = dom.select("#ProductDetails li div");
      if (CollectionUtils.isNotEmpty(specsEl)) {
        specs = new HashSet<>();
        for (int i = 0; i < specsEl.size(); i++) {
          String key = specsEl.get(i).text();
          String value = "";
          if (i < specsEl.size() - 1) {
            value = specsEl.get(++i).text();
          }
          specs.add(new LinkSpec(key, value));
        }
      } else {
      	specs = getMultipleKeyValueSpecs(dom.select(".ux-layout-section--features .ux-layout-section__row"), "ux-labels-values__labels", "ux-labels-values__values");
      }
    }

  	brand = GlobalConsts.NOT_AVAILABLE;
    if (CollectionUtils.isNotEmpty(specs)) {
    	specs.forEach(spec -> {
  	    if (spec.getKey().matches(BRAND_WORDS)) {
  	      brand = spec.getValue();
  	    }
    	});
    }
  }

}
