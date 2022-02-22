package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * MediaMarkt Global Type 2
 *
 * URL depends on the country
 * NL --> https://www.mediamarkt.nl
 * TR --> https://www.mediamarkt.com.tr
 *
 * @author mdpinar
 */
public class MediaMarktXX_2 extends AbstractWebsite {

	private Document dom;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element pageType = dom.selectFirst("meta[property='pageTypeId']");
		if (pageType != null && pageType.hasAttr("content") && pageType.attr("content").contains("generic") == false) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[itemprop='availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
    	String content = val.attr("content").toLowerCase();
      return (content.contains("instock") || content.contains("preorder"));
    }

    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[property='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    if (val == null) val = dom.selectFirst("span[itemprop='sku']");
    if (val != null && val.hasAttr("content")) {
      return val.attr("content").replaceAll("sku:", "");
    }
    
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null) {
      return val.text();
    }

    val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
    	return val.attr("content");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }
	
  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='price']");
    if (val == null) val = dom.selectFirst("meta[property='product:price:amount']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val == null) val = dom.selectFirst("meta[property='brand']");
    if (val == null) val = dom.selectFirst("meta[property='product:brand']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element shipment = dom.selectFirst("div.price.big");
    if (shipment != null) {
    	try {
	      Element val = shipment.nextElementSibling().selectFirst("small");
	      if (val != null && StringUtils.isNotBlank(val.text())) {
	        return val.text();
	      }
			} catch (Exception e) { }
    }

    shipment = dom.selectFirst("div.old-price-block");
    if (shipment != null) {
    	try {
	      Element val = shipment.nextElementSibling().selectFirst("small");
	      if (val != null && StringUtils.isNotBlank(val.text())) {
	        return val.text();
	      }
			} catch (Exception e) { }
    }

    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getKeyValueSpecs(dom.select("dl.specification"), "dt", "dd");
  }

}
