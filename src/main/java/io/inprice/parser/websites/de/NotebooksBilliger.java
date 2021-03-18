package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for NotebooksBilliger Deutschland
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class NotebooksBilliger extends AbstractWebsite {

	private Document dom;
	private String brandName;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

		dom = Jsoup.parse(html);
		brandName = findAPart(html, "\"productBrand\":\"", "\"");
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("div.availability_widget span.list_names");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().contains("Abholbereit");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("div#product_page_detail");
    if (val != null && StringUtils.isNotBlank(val.attr("data-products-number"))) {
      return val.attr("data-products-number");
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
    Element val = dom.getElementById("product_detail_price");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (StringUtils.isNotBlank(brandName)) {
      return brandName;
    }

    Element val = dom.selectFirst("div.product_headline div.image_container img");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      String alt = val.attr("alt");
      return alt.substring(0, alt.lastIndexOf(" "));
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    return "Notebooks Billiger";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div.sameday img");
    if (val != null && StringUtils.isNotBlank(val.attr("alt"))) {
      return val.attr("alt");
    }
    return "Abholung im Geschäft";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = getValueOnlySpecList(dom.select("div#section_info li span"));
    if (specList == null)
      specList = getKeyValueSpecList(dom.select("table.properties_table tr"), "td.produktDetails_eigenschaft2",
          "td.produktDetails_eigenschaft3");

    return specList;
  }

}
