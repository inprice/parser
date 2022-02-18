package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
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
import io.inprice.common.utils.NumberHelper;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Etsy, USA
 *
 * https://www.etsy.com
 *
 * @author mdpinar
 */
public class EtsyUS extends AbstractWebsite {

	private Document dom;

	@Override
	protected Renderer getRenderer() {
		return Renderer.NODE_FETCH;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element notFoundMain = dom.selectFirst(".error-page-panel");
		if (notFoundMain == null) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("input[name='quantity']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return NumberHelper.toInteger(cleanDigits(val.attr("value")), 0) > 0;
    }

    Elements availabilities = dom.select("select#inventory-variation-select-quantity option");
    return (CollectionUtils.isNotEmpty(availabilities));
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("input[name='listing_id']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return val.attr("value");
    }

    val = dom.selectFirst("h1[data-listing-id]");
    if (val != null && StringUtils.isNotBlank(val.attr("data-listing-id"))) {
      return val.attr("data-listing-id");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val == null) val = dom.selectFirst("meta[property='etsymarketplace:price_value']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

  	val = dom.selectFirst("span.override-listing-price");
    
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	Element val = dom.selectFirst("a[aria-label='Contact the shop']");
    if (val != null && StringUtils.isNotBlank(val.attr("data-to_user_display_name"))) {
      return val.attr("data-to_user_display_name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("a[href$='shopname']");
    if (val != null) {
      return val.text();
    }
    return getBrand();
  }

  @Override
  public String getShipment() {
    Elements vals = dom.select("div[data-delivery-data] .wt-text-caption, div[data-delivery-data] .wt-text-body-03");
    if (vals != null) {
    	LinkedHashSet<String> set = new LinkedHashSet<>(vals.size());
    	for (int i = 0; i < vals.size(); i++) {
    		Element val = vals.get(i);
    		if (StringUtils.isNotBlank(val.text())) set.add(val.text().trim());
			}
    	return String.join(". ", set);
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getValueOnlySpecs(dom.select("div#product-details-content-toggle div.wt-ml-xs-2"));
  	if (specs == null || specs.size() == 0) specs = getValueOnlySpecs(dom.select("div.listing-page-overview-component p"));
  	
  	return specs;
  }

}
