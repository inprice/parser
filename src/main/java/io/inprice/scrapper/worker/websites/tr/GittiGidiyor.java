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
 * Parser for GittiGidiyor Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class GittiGidiyor extends AbstractWebsite {

  public GittiGidiyor(Link link) {
    super(link);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.getElementById("VariantProductRemaingCount");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.selectFirst(".remainingCount");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      try {
        int realAmount = new Integer(cleanDigits(val.text()));
        return (realAmount > 0);
      } catch (Exception e) {
        //
      }
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.getElementById("productId");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.getElementById("sp-title");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.selectFirst("span.title");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.getElementById("productTitle");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("span.lastPrice");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    } else {
      val = doc.selectFirst("[data-price]");
      if (val != null && StringUtils.isNotBlank(val.attr("data-price"))) {
        return new BigDecimal(cleanDigits(val.attr("data-price")));
      }
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.getElementById("sp-member-nick");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.selectFirst(".member-name a strong");
    
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = doc.getElementById("sp-tabContent-shipping-type-text");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.selectFirst(".CargoInfos");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("ul.product-items li a");
    if (val == null || StringUtils.isBlank(val.text())) val = doc.selectFirst(".mr10.gt-product-brand-0 a");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(doc.select("#specs-container ul li"), "span", "strong");
  }

}
