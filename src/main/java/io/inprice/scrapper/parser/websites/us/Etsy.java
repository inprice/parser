package io.inprice.scrapper.parser.websites.us;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.parser.helpers.Consts;
import io.inprice.scrapper.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Etsy USA
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Etsy extends AbstractWebsite {

  public Etsy(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("input[name='quantity']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      try {
        int qty = new Integer(cleanDigits(val.attr("value")));
        return qty > 0;
      } catch (Exception e) {
        //
      }
    }

    Elements availabilities = doc.select("select#inventory-variation-select-quantity option");
    return (availabilities != null && availabilities.size() > 0);
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("input[name='listing_id']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return val.attr("value");
    }

    val = doc.selectFirst("h1[data-listing-id]");
    if (val != null && StringUtils.isNotBlank(val.attr("data-listing-id"))) {
      return val.attr("data-listing-id");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("span.override-listing-price");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    val = doc.selectFirst("meta[property='etsymarketplace:price_value']");
    if (val == null || StringUtils.isBlank(val.attr("content"))) {
      val = doc.selectFirst("meta[property='product:price:amount']");
    }

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("a[aria-label='Contact the shop']");
    if (val != null && StringUtils.isNotBlank(val.attr("data-to_username"))) {
      return val.attr("data-to_username");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    StringBuilder sb = new StringBuilder();
    Element val = doc.selectFirst("div.js-estimated-delivery div");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text().trim());
      sb.append(". ");
    }

    val = doc.selectFirst("div.js-ships-from");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text().trim());
      sb.append(". ");
    }

    val = doc.selectFirst("div.shipping-cost");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text().trim());
      sb.append(". ");
    }

    if (sb.length() == 0) {
      sb.append("NA");
    }

    return sb.toString();
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("a[aria-label='Contact the shop']");
    if (val != null && StringUtils.isNotBlank(val.attr("data-to_user_display_name"))) {
      return val.attr("data-to_user_display_name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.listing-page-overview-component p"));
  }

}
