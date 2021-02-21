package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

  /**
   * Returns availability status looking at html body
   *
   * @return boolean - availability status
   */
  @Override
  public boolean isAvailable() {
    return doc.html().contains("\"availabilityStatus\":\"IN_STOCK\"");
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("a[data-tl-id='ProductSellerInfo-SellerName']");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return "Walmart";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst(".free-shipping-msg");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("div.prod-pickupMessageAccess span");
    }
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("span.copy-small.font-bold");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("span[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div#product-about li"));
  }

}
