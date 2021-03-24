package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Walmart USA
 *
 * Contains standard data. all (except for availability) is extracted by css
 * selectors
 *
 * @author mdpinar
 */
public class Walmart extends AbstractWebsite {

	private Document dom;
	private boolean isAvailable;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

		dom = Jsoup.parse(html);
		isAvailable = (html.indexOf("\"availabilityStatus\":\"IN_STOCK\"") > 0);
	}

  /**
   * Returns availability status looking at html body
   *
   * @return boolean - availability status
   */
  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("span[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("a[data-tl-id='ProductSellerInfo-SellerName']");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst(".free-shipping-msg");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst("div.prod-pickupMessageAccess span");
    }
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst("span.copy-small.font-bold");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	List<LinkSpec> specList = null;
  	
  	List<LinkSpec> specs = getValueOnlySpecList(dom.select("div#product-about li"));
  	if (specs == null || specs.size() == 0) specs = getValueOnlySpecList(dom.select(".about-product-description li"));
  	
  	if (specs != null && specs.size() > 0) {
  		specList = new ArrayList<>(specs.size());
  		for (int i = 0; i < specs.size(); i++) {
				LinkSpec spec = specs.get(i);
				if (spec.getValue().contains(":")) {
					String[] pair = spec.getValue().split(":");
					spec.setKey(pair[0]);
					spec.setValue(pair[1]);
				}
				specList.add(spec);
			}
  	}
  	
  	return specList;
  }

}
