package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for n11 Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class N11 extends AbstractWebsite {

  @Override
  public boolean isAvailable() {
    String value = null;

    Element val = doc.getElementById("skuStock");
    if (val != null) {
      if (StringUtils.isNotBlank(val.text())) {
        value = val.text();
      } else if (StringUtils.isNotBlank(val.val())) {
        value = val.val();
      }
    }
    
    if (StringUtils.isBlank(value)) {
      val = doc.selectFirst("input[class='stockCount']");
      if (val != null && StringUtils.isNotBlank(val.val())) {
        value = val.val();
      } else {
        val = doc.selectFirst(".stockWarning");
        if (val != null && StringUtils.isNotBlank(val.val())) {
          value = val.val();
        } else {
          val = doc.selectFirst(".newPrice ins");
          if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
            value = val.attr("content");
          }
        }
      }
    }

    if (value != null && StringUtils.isNotBlank(value)) {
      try {
        int realAmount = new Integer(cleanDigits(value));
        return (realAmount > 0);
      } catch (Exception ignored) { }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("input[class='productId']");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("h1.proName");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("h1.pro-title_main");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    String value = null;

    Element val = doc.getElementById("skuPrice");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      value = val.val();
    } else {
      val = doc.selectFirst(".newPrice ins");
      if (val == null || StringUtils.isBlank(val.attr("content"))) val = doc.selectFirst("ins.price-now");
      if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
        value = val.attr("content");
      }
    }

    if (value != null && StringUtils.isNotBlank(value)) {
      return new BigDecimal(cleanDigits(value));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    String value = null;

    Element val = doc.selectFirst("div.sallerTop h3 a");
    if (val != null && StringUtils.isNotBlank(val.attr("title"))) {
      value = val.attr("title");
    } else {
      val = doc.selectFirst(".shop-name");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        value = val.text();
      }
    }

    if (StringUtils.isBlank(value)) {
      val = doc.selectFirst("a.main-seller-name");
      if (val != null && StringUtils.isNotBlank(val.attr("title"))) {
        value = val.attr("title");
      }
    }

    return value;
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst(".shipment-detail-container .cargoType");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst(".delivery-info_shipment span");
    }

    if (val != null) {
      return val.text().replaceAll(":", "");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("span.label:contains(Marka)");
    if (val == null) val = doc.selectFirst("span.label:contains(Yazar)");

    if (val != null) {
      Element sbling = val.nextElementSibling();
      if (sbling != null && StringUtils.isNotBlank(sbling.text())) {
        return sbling.text();
      }
    }

    String[] titleChunks = getName().split("\\s");
    if (titleChunks.length > 0)
      return titleChunks[0];

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    Elements specs = doc.select("div.feaItem");
    String keySelector = ".label";
    String valSelector = ".data";

    if (specs == null || specs.isEmpty()) {
      specs = doc.select("li.unf-prop-list-item");
      keySelector = "p.unf-prop-list-title";
      valSelector = "p.unf-prop-list-prop";
    }

    return getKeyValueSpecList(specs, keySelector, valSelector);
  }
}