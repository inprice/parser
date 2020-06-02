package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for NotebooksBilliger Deutschland
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class NotebooksBilliger extends AbstractWebsite {

  public NotebooksBilliger(Competitor competitor) {
    super(competitor);
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("div.availability_widget span.list_names");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().contains("Abholbereit");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("div#product_page_detail");
    if (val != null && StringUtils.isNotBlank(val.attr("data-products-number"))) {
      return val.attr("data-products-number");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.getElementById("product_detail_price");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Notebooks Billiger";
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("div.sameday img");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      return val.attr("alt");
    }
    return "Abholung im Geschäft";
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("div.product_headline div.image_container img");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      String alt = val.attr("alt");
      return alt.substring(0, alt.lastIndexOf(" "));
    }

    final String brandName = findAPart(doc.html(), "\"productBrand\":\"", "\"");
    if (StringUtils.isNotBlank(brandName)) {
      return brandName;
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = getValueOnlySpecList(doc.select("div#section_info li span"));
    if (specList == null)
      specList = getKeyValueSpecList(doc.select("table.properties_table tr"), "td.produktDetails_eigenschaft2",
          "td.produktDetails_eigenschaft3");

    return specList;
  }
}
