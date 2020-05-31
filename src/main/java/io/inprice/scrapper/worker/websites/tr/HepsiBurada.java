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
 * Parser for HepsiBurada Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class HepsiBurada extends AbstractWebsite {

  public HepsiBurada(Link link) {
    super(link);
  }

  @Override
  public boolean isAvailable() {
    return isTrue("\"isInStock\":");
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("#addToCartForm input[name='sku']");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.getElementById("product-name");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("span[itemprop='name']");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
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
    Element val = doc.selectFirst("span.seller a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("label.campaign-text span");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    boolean freeShipping = isTrue("\"freeShipping\":");
    return "Ücretsiz Kargo: " + (freeShipping ? "Evet" : "Hayır");
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("span[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(doc.select(".data-list.tech-spec tr"), "th", "td");
  }

  private boolean isTrue(String indicator) {
    final String result = findAPart(doc.html(), indicator, ",");
    return "true".equalsIgnoreCase(result);
  }

}
