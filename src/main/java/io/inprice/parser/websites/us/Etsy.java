package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Etsy USA
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Etsy extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("input[name='quantity']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return NumberUtils.toInteger(cleanDigits(val.attr("value")), 0) > 0;
    }

    Elements availabilities = dom.select("select#inventory-variation-select-quantity option");
    return (availabilities != null && availabilities.size() > 0);
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
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("span.override-listing-price");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    val = dom.selectFirst("meta[property='etsymarketplace:price_value']");
    if (val == null || StringUtils.isBlank(val.attr("content"))) {
      val = dom.selectFirst("meta[property='product:price:amount']");
    }

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("a[aria-label='Contact the shop']");
    if (val != null && StringUtils.isNotBlank(val.attr("data-to_user_display_name"))) {
      return val.attr("data-to_user_display_name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("a[aria-label='Contact the shop']");
    if (val != null && StringUtils.isNotBlank(val.attr("data-to_username"))) {
      return val.attr("data-to_username");
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    StringBuilder sb = new StringBuilder();
    Element val = dom.selectFirst("div.js-estimated-delivery div");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text().trim());
      sb.append(". ");
    }

    val = dom.selectFirst("div.js-ships-from");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text().trim());
      sb.append(". ");
    }

    val = dom.selectFirst("div.shipping-cost");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text().trim());
      sb.append(". ");
    }

    if (sb.length() == 0) {
      sb.append(Consts.Words.NOT_AVAILABLE);
    }

    return sb.toString();
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(dom.select("div.listing-page-overview-component p"));
  }

}
