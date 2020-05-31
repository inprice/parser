package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Amazon Global
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * finding by asin : https://www.amazon.com/dp/B00VX62MHO
 *
 * @author mdpinar
 */
public class Amazon extends AbstractWebsite {

  public Amazon(Link link) {
    super(link);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.getElementById("availability");
    if (val != null) {
      Element span = val.selectFirst("span.a-color-success");
      if (span == null)
        span = val.selectFirst("span.a-color-price");
      return span != null;
    }

    val = doc.getElementById("ebooksProductTitle");
    if (val == null) val = doc.getElementById("add-to-cart-button");

    return (val != null);
  }

  @Override
  public String getSku() {
    Element val = doc.getElementById("ASIN");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }

    val = doc.selectFirst("input[name='ASIN.0']");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.getElementById("productTitle");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.getElementById("ebooksProductTitle");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  /**
   * The reason why this method became so complicated.
   *
   * Amazon has different types of page designs. Price can be found in different
   * locations. Sometimes it can be a range like $100 - $300, or the product is on
   * sale which is required to mark with extra css and tags. So, we need to
   * consider all those possibilities to have the correct price info.
   *
   * @return BigDecimal - the price
   */
  @Override
  public BigDecimal getPrice() {
    String strPrice = null;

    Element val = doc.getElementById("priceblock_dealprice");
    if (val == null) val = doc.getElementById("priceblock_ourprice");

    if (val != null) {
      Element integer = val.selectFirst("span.price-large");
      if (integer != null) {
        Element decimal = integer.nextElementSibling();
        if (decimal != null) {
          strPrice = integer.text().trim() + "." + decimal.text().trim();
          return new BigDecimal(cleanDigits(strPrice));
        }
      }
    }

    if (val == null) val = doc.selectFirst("div#buybox span.a-color-price");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      strPrice = val.text();
    } else {
      val = doc.getElementById("cerberus-data-metrics");
      if (val != null && StringUtils.isNotBlank(val.attr("data-asin-price"))) {
        strPrice = val.attr("data-asin-price");
      }
    }

    if (strPrice == null || StringUtils.isBlank(strPrice)) {
      val = doc.selectFirst(".header-price");
      if (val == null || StringUtils.isBlank(val.text())) {
        val = doc.selectFirst("span.a-size-base.a-color-price.a-color-price");
      }
      if (val == null || StringUtils.isBlank(val.text())) {
        val = doc.selectFirst(".a-size-medium.a-color-price.offer-price.a-text-normal");
      }

      if (val != null && StringUtils.isNotBlank(val.text())) {
        strPrice = val.text();
      } else {
        val = doc.selectFirst(".price-large");
        if (val != null && StringUtils.isNotBlank(val.text())) {
          String left = cleanDigits(val.text());
          String right = "00";
          if (val.nextElementSibling() != null) {
            right = val.nextElementSibling().text();
          }
          strPrice = left + "." + right;
        } else {
          // if val is a range like 100 - 300
          val = doc.getElementById("priceblock_ourprice");
        }
      }
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      if (val.text().contains("-")) {
        String[] priceChunks = val.text().split("-");
        String first = cleanDigits(priceChunks[0]);
        String second = cleanDigits(priceChunks[1]);
        BigDecimal low = new BigDecimal(cleanDigits(first));
        BigDecimal high = new BigDecimal(cleanDigits(second));
        strPrice = high.add(low).divide(BigDecimal.valueOf(2)).toString();
      } else {
        strPrice = val.text();
      }
    }

    if (strPrice == null || StringUtils.isBlank(strPrice)) {
      return BigDecimal.ZERO;
    } else {
      return new BigDecimal(cleanDigits(strPrice));
    }
  }

  @Override
  public String getSeller() {
    Element val = doc.getElementById("sellerProfileTriggerId");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("span.mbcMerchantName");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Amazon";
  }

  @Override
  public String getShipment() {
    Element val = doc.getElementById("price-shipping-message");

    if (val == null || StringUtils.isBlank(val.text())) val = doc.selectFirst(".shipping3P");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("mbc-shipping-free-1");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("mbc-shipping-sss-returns-free-1");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("mbc-shipping-sss-eligible-1");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("ddmDeliveryMessage");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("deliverTo");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("delivery-message");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.getElementById("buybox-see-all-buying-choices-announce");
    if (val != null) {
      return "See all offers";
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.getElementById("mbc");
    if (val != null && StringUtils.isNotBlank(val.attr("data-brand"))) {
      return val.attr("data-brand");
    }

    val = doc.getElementById("bylineInfo");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("span.ac-keyword-link a");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return "Amazon";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = getValueOnlySpecList(doc.select("#feature-bullets li:not(.aok-hidden)"));
    if (specList == null) {
      specList = getValueOnlySpecList(doc.select("div.content ul li"));
    }
    return specList;
  }
}
