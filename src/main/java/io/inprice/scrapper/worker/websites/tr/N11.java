package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for n11 Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class N11 extends AbstractWebsite {

  public N11(Link link) {
    super(link);
  }

  @Override
  public boolean isAvailable() {
    String value = null;

    Element val = doc.selectFirst("input[class='stockCount']");
    if (val == null || StringUtils.isBlank(val.val())) {
      val = doc.selectFirst(".stockWarning");
      value = val.text();
    } else {
      value = val.val();
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
    Element val = doc.selectFirst(".newPrice ins");
    if (val == null || StringUtils.isBlank(val.attr("content"))) {
      val = doc.selectFirst("ins.price-now");
    }

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
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
    return getKeyValueSpecList(doc.select("div.feaItem"), ".label", ".data");
  }
}
