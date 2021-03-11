package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Bonprix Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Bonprix extends AbstractWebsite {

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("meta[property='og:availability']");
    if (val == null) val = doc.selectFirst("meta[content='https://schema.org/InStock']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content").toLowerCase().contains("instock");
    }

    val = doc.selectFirst("div.product-availability-box_wrapper div");
    if (val == null) doc.selectFirst("div#product-detail-availibility-container noscript");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().contains("erfügbar");
    }

    return false;
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
    Element val = doc.selectFirst("h1.product-name span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("span.price");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    val = doc.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    val = doc.selectFirst("span.clearfix.price");
    if (val == null) val = doc.selectFirst("meta[property='og:price:amount']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Bonprix";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div[data-controller='infoicon'] div.info-text");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll(" ada12_info-icon-bigsize-additional-text", "");
    }

    val = doc.selectFirst("div.product-availability-box_wrapper div");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.getElementById("aiDelChargeSame");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("meta[itemprop='brand']");
    if (val == null) val = doc.selectFirst("meta[property='og:brand']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specKeys = doc.select("div.product-attributes strong");
    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      Elements specValues = doc.select("div.product-attributes span");
      for (int i = 0; i < specKeys.size(); i++) {
        Element key = specKeys.get(i);
        Element val = null;
        if (i < specValues.size() - 1) {
          val = specValues.get(i);
        }
        specList.add(new LinkSpec(key.text().replaceAll(":", ""), (val != null ? val.text() : "")));
      }
      return specList;
    }

    specKeys = doc.select("div.productFeaturesContainer span.productFeatureName");
    if (specKeys == null)
      specKeys = doc.select("div.product-attributes strong");

    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      for (Element key : specKeys) {
        specList.add(new LinkSpec(key.text().replaceAll(":", ""), ""));
      }
    }

    Elements specValues = doc.select("div.productFeaturesContainer span.productFeatureValue");

    if (specValues != null && specValues.size() > 0) {
      boolean isEmpty = false;
      if (specList == null) {
        isEmpty = true;
        specList = new ArrayList<>();
      }
      for (int i = 0; i < specValues.size(); i++) {
        Element value = specValues.get(i);
        if (isEmpty) {
          specList.add(new LinkSpec("", value.text()));
        } else {
          specList.get(i).setValue(value.text());
        }
      }

      return specList;
    }

    specValues = doc.select("div.productDescription");

    if (specValues != null) {
      specList = new ArrayList<>();
      String desc = specValues.text();
      String[] descChunks = desc.split("\\.");
      if (descChunks.length > 0) {
        for (String dsc : descChunks) {
          specList.add(new LinkSpec("", dsc));
        }
      }
    }

    return specList;
  }

}
