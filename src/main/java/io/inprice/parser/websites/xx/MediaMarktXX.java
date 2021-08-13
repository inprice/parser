package io.inprice.parser.websites.xx;

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
 * MediaMarkt Global
 *
 * https://www.mediamarkt.com
 *
 * @author mdpinar
 */
public class MediaMarktXX extends AbstractWebsite {

	private Document dom;
  private BigDecimal freeShippingTresholdForNL;
	
	@Override
	protected void setHtml(String html) {
		dom = Jsoup.parse(html);

    String tresholdForNL = findAPart(html, "bezorgkostenDrempel =", ";");
    if (tresholdForNL != null) {
      freeShippingTresholdForNL = new BigDecimal(cleanDigits(tresholdForNL));
    }
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst(".online-nostock");
    if (val != null) {
      return false;
    }

    val = dom.selectFirst("meta[property='og:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return !val.attr("content").trim().equals("out of stock");
    }

    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("dd span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
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
    Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("meta[property='og:site_name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    if (freeShippingTresholdForNL != null) {
      if (freeShippingTresholdForNL.compareTo(getPrice()) > 0) {
        return "Gratis bezorging vanaf € " + freeShippingTresholdForNL.toString();
      } else {
        return "Gratis bezorging";
      }
    }

    Element shipment = dom.selectFirst("div.price.big");
    if (shipment != null) {
      Element val = shipment.nextElementSibling().selectFirst("small");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    shipment = dom.selectFirst("div.old-price-block");
    if (shipment != null) {
      Element val = shipment.nextElementSibling().selectFirst("small");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    String parentClass = "specification";
    Elements isParentExist = dom.select("dl." + parentClass);
    if (isParentExist == null)
      parentClass = "product-details";

    Elements specKeys = dom.select(String.format("dl.%s dt", parentClass));
    if (specKeys != null && specKeys.size() > 0) {
      specs = new HashSet<>();
      for (Element key : specKeys) {
        specs.add(new LinkSpec(key.text().replaceAll(":", ""), ""));
      }
    }

    Elements specValues = dom.select(String.format("dl.%s dd", parentClass));
    if (specValues != null && specValues.size() > 0) {
      boolean isEmpty = false;
      if (specs == null) {
        isEmpty = true;
        specs = new HashSet<>();
      }
      for (int i = 0; i < specs.size(); i++) {
        Element value = specValues.get(i);
        if (isEmpty) {
          specs.add(new LinkSpec("", value.text()));
        //} else {
        //  specs.get(i).setValue(value.text());
        }
      }
    }

    if (specs == null) {
      specValues = dom.select("p.autoWrapParagraph p");
      if (specValues != null && specValues.size() > 0) {
        specs = new HashSet<>();
        for (Element spec : specValues) {
          specs.add(new LinkSpec("", spec.text()));
        }
      }
    }

    return specs;
  }

}
