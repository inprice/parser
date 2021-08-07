package io.inprice.parser.websites.it;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Euronics Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class EuronicsIT extends AbstractWebsite {

	private Document dom;
	private Element prod;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

		dom = Jsoup.parse(html);
    prod = dom.getElementsByTag("trackingProduct").first();
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("span.productDetails__availability.not-available");
    return (val == null || StringUtils.isBlank(val.text()));
  }

  @Override
  public String getSku() {
    if (prod != null) {
      return prod.attr("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (prod != null) {
      return prod.attr("productName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (prod != null) {
      return new BigDecimal(cleanDigits(prod.attr("price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (prod != null) {
      return prod.attr("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("span.productDetails__label.productDetails__label--left");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      StringBuilder sb = new StringBuilder();

      sb.append(val.text());
      sb.append(" ");

      val = dom.selectFirst("span.productDetails__label.productDetails__label--right");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        sb.append(val.text());
      }

      return sb.toString();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

  	Elements specsEl = dom.select("div.product__specificationsContent .content__abstractText");
  	if (specsEl != null && specsEl.size() > 0) {
  		specs = new HashSet<>(specsEl.size());
  		for (int i = 0; i < specsEl.size(); i++) {
				Element spec = specsEl.get(i);

  			String key = spec.selectFirst(".product__specificationItem").text();

  			String val = "";
  			Element valEL = spec.selectFirst(".product__specificationItemDetail");
  			if (valEL.hasClass("i-check")) {
  				val = "Yes";
  			} if (valEL.hasClass("i-accordion_close")) {
  				val = "No";
  			} else {
  				val = valEL.text();
  			}
  			specs.add(new LinkSpec(key, val));
  		}
  	}

  	if (specs == null || specs.size() == 0) {
  		specs = getValueOnlySpecs(dom.select("ul.productDetails__specifications li"));
  	}
  	
  	return specs;
  }

}
