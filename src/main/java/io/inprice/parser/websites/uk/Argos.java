package io.inprice.parser.websites.uk;

import io.inprice.common.models.Competitor;
import io.inprice.common.models.CompetitorSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Argos UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Argos extends AbstractWebsite {

  public Argos(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    final String availability = findAPart(doc.html(), "\"globallyOutOfStock\":", ",");
    return "false".equalsIgnoreCase(availability);
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("[itemProp='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("span.product-title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("[data-test='product-title']");
    if (val != null && StringUtils.isNotBlank(val.html())) {
      return val.html();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst(".product-price-primary");
    if (val == null || StringUtils.isBlank(val.attr("content"))) val = doc.selectFirst("[itemProp='price']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Argos";
  }

  @Override
  public String getShipment() {
    final String staticPart = "In-store pickup";

    Element val = doc.selectFirst("a.ac-propbar__slot > span.sr-only");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return staticPart + " OR " + val.text();
    }

    return staticPart;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    final String brandName = findAPart(doc.html(), "\"brand\":\"", "\",");
    if (brandName != null) {
      return brandName;
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select(".product-description-content-text li"));
  }
}
