package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * Amazon, Global
 *
 * https://www.amazon.com
 *
 * @author mdpinar
 */
public class AmazonXX extends AbstractWebsite {

	private Document dom;
	
	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		
		Element titleEL = dom.getElementById("title");
		if (titleEL != null) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}
	
  @Override
  public boolean isAvailable() {
    Element val = dom.getElementById("availability");
    if (val != null) {
      Element span = val.selectFirst("span.a-color-success");
      if (span == null) span = val.selectFirst("span.a-color-price");
      if (span != null) return true;

      return (val.text().toLowerCase().indexOf("in stock") > -1);
    }

    val = dom.getElementById("ebooksProductTitle");
    if (val == null) val = dom.getElementById("add-to-cart-button");

    return (val != null);
  }

  @Override
  public String getSku() {
  	Element val = dom.getElementById("attach-baseAsin");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return val.attr("value");
    }

  	val = dom.getElementById("ASIN");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }

    val = dom.selectFirst("input[name='asin']");
		if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
   		return val.attr("value");
		}

    val = dom.selectFirst("input[name='ASIN.0']");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }
		
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	Element val = dom.getElementById("title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
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
    
    Element price = dom.getElementById("attach-base-product-price");
    if (price == null) price = dom.getElementById("twister-plus-price-data-price");

    if (price != null) {
    	strPrice = price.attr("value");
    }

    if (price == null) {
	    price = dom.getElementById("priceblock_dealprice");
	    if (price == null) {
	      price = dom.getElementById("priceblock_ourprice");
	      if (price != null) {
	      	price = dom.getElementById("price_inside_buybox");
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
    }

    if (price == null) {
      price = dom.selectFirst("div#buybox span.a-color-price");
    }

    if (StringUtils.isBlank(strPrice)) {
    	if (price != null) {
	      strPrice = price.text();
	    } else {
	      price = dom.getElementById("cerberus-data-metrics");
	      if (price != null) {
	        strPrice = price.attr("data-asin-price");
	      }
	    }
    }

    if (StringUtils.isBlank(strPrice)) {
      price = dom.selectFirst(".header-price");
      if (price == null)
        price = dom.selectFirst("span.a-size-base.a-color-price.a-color-price");
      if (price == null)
        price = dom.selectFirst(".a-size-medium.a-color-price.offer-price.a-text-normal");

      if (price != null) {
        strPrice = price.text();
      } else {
        price = dom.selectFirst(".price-large");
        if (price != null) {
          String left = cleanDigits(price.text());
          String right = "00";
          if (price.nextElementSibling() != null) {
            right = price.nextElementSibling().text();
          }
          strPrice = left + "." + right;
        } else {
          // if price is a range like 100 - 300
          price = dom.getElementById("priceblock_ourprice");
        }
      }
    }

    if (price != null && price.text().contains("-")) {
      String[] priceChunks = price.text().split("-");
      String first = cleanDigits(priceChunks[0]);
      String second = cleanDigits(priceChunks[1]);
      BigDecimal low = new BigDecimal(cleanDigits(first));
      BigDecimal high = new BigDecimal(cleanDigits(second));
      strPrice = high.add(low).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).toString();
    }

    if (strPrice == null || strPrice.isEmpty())
      return BigDecimal.ZERO;
    else
      return new BigDecimal(cleanDigits(strPrice));
  }

  @Override
  public String getBrand() {
    Element val = dom.getElementById("mbc");
    if (val != null && StringUtils.isNotBlank(val.attr("data-brand"))) {
      return val.attr("data-brand");
    }

    val = dom.selectFirst("tr.po-brand td.a-span9 span");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    
    val = dom.getElementById("bylineInfo");
    if (val == null) val = dom.selectFirst("span.ac-keyword-link a");
    if (val == null) val = dom.selectFirst("span[data-hook='cm_cr_skyfall_medley_group']");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll("Brand..", "");
    }

    return "Amazon";
  }

  @Override
  public String getSeller() {
  	Element val = dom.getElementById("sellerProfileTriggerId");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst("span.mbcMerchantName");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Element val = dom.getElementById("price-shipping-message");

    if (val == null || StringUtils.isBlank(val.text())) val = dom.selectFirst(".shipping3P");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("shippingMessage_feature_div");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("mbc-shipping-free-1");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("mbc-shipping-sss-returns-free-1");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("mbc-shipping-sss-eligible-1");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("ddmDeliveryMessage");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("deliverTo");
    if (val == null || StringUtils.isBlank(val.text())) val = dom.getElementById("delivery-message");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll("Learn more", "");
    }

    val = dom.getElementById("buybox-see-all-buying-choices-announce");
    if (val != null) {
      return "See all offers";
    }

    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select("table.prodDetTable tr"), "th", "td");
    if (specs != null) {
      return specs;
    }
  	
  	specs = getValueOnlySpecs(dom.select("#feature-bullets li:not(.aok-hidden)"));
    if (specs == null) {
      specs = getValueOnlySpecs(dom.select("div.content ul li"));
    }
    return specs;
  }

}
