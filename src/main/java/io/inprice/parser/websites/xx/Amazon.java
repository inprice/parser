package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Amazon Global
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * finding by asin : https://www.amazon.com/dp/B00VX62MHO
 *
 * @author mdpinar
 */
public abstract class Amazon extends AbstractWebsite {

  @Override
  public boolean isAvailable() {
    Element val = doc.getElementById("availability");
    if (val != null) {
      Element span = val.selectFirst("span.a-color-success");
      if (span == null) {
        span = val.selectFirst("span.a-color-price");
      }
      if (span != null) return true;

      return (val.text().toLowerCase().indexOf("in stock") > -1);
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

    Element price = doc.getElementById("priceblock_dealprice");
    if (price == null) {
      price = doc.getElementById("priceblock_ourprice");
      if (price != null) {
      	price = doc.getElementById("price_inside_buybox");
      	if (price != null) {
          Element integer = price.selectFirst("span.price-large");
          if (integer != null) {
            Element decimal = integer.nextElementSibling();
            if (decimal != null) {
              strPrice = integer.text().trim() + "." + decimal.text().trim();
              return new BigDecimal(cleanDigits(strPrice));
            }
          }
        }
      }
    }

    if (price == null)
      price = doc.selectFirst("div#buybox span.a-color-price");

    if (price != null) {
      strPrice = price.text();
    } else {
      price = doc.getElementById("cerberus-data-metrics");
      if (price != null)
        strPrice = price.attr("data-asin-price");
    }

    if (strPrice == null || strPrice.isEmpty()) {
      price = doc.selectFirst(".header-price");
      if (price == null)
        price = doc.selectFirst("span.a-size-base.a-color-price.a-color-price");
      if (price == null)
        price = doc.selectFirst(".a-size-medium.a-color-price.offer-price.a-text-normal");

      if (price != null) {
        strPrice = price.text();
      } else {
        price = doc.selectFirst(".price-large");
        if (price != null) {
          String left = cleanDigits(price.text());
          String right = "00";
          if (price.nextElementSibling() != null) {
            right = price.nextElementSibling().text();
          }
          strPrice = left + "." + right;
        } else {
          // if price is a range like 100 - 300
          price = doc.getElementById("priceblock_ourprice");
        }
      }
    }

    if (price != null) {
      if (price.text().contains("-")) {
        String[] priceChunks = price.text().split("-");
        String first = cleanDigits(priceChunks[0]);
        String second = cleanDigits(priceChunks[1]);
        BigDecimal low = new BigDecimal(cleanDigits(first));
        BigDecimal high = new BigDecimal(cleanDigits(second));
        strPrice = high.add(low).divide(BigDecimal.valueOf(2)).setScale(2, RoundingMode.HALF_UP).toString();
      } else {
        strPrice = price.text();
      }
    }

    if (strPrice == null || strPrice.isEmpty())
      return BigDecimal.ZERO;
    else
      return new BigDecimal(cleanDigits(strPrice));
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
      return val.text().replaceAll("Learn more", "");
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

    val = doc.selectFirst("span[data-hook='cm_cr_skyfall_medley_group']");
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
