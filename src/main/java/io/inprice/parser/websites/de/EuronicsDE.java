package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Euronics, Germany
 *
 * https://www.euronics.de
 *
 * @author mdpinar
 */
public class EuronicsDE extends AbstractWebsite {

	private Document dom;

	@Override
	protected By waitBy() {
		return By.id("product-price");
	}
	
	@Override
	protected ParseStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		String title = dom.title();
		if (title.toLowerCase().contains("fehler 404") == false) {
			return ParseStatus.PS_OK;
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("link[itemprop='availability']");
    if (val != null && val.hasAttr("href")) {
      String href = val.attr("href").toLowerCase();
      return href.contains("instock") || href.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku(String url) {
  	String[] chunks = url.split("-");
    return chunks[chunks.length-1];
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst(".product--title");
    if (val != null) {
    	return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.getElementById("product-price");
    if (val != null && StringUtils.isNotBlank(val.attr("data-article-price"))) {
      return new BigDecimal(cleanDigits(val.attr("data-article-price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
  	Element val = dom.selectFirst(".shipping--cost");
  	if (val != null) return val.text();
  	
  	val = dom.selectFirst(".shipping--is-free");
  	if (val != null) return val.text();
  	
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select(".feature-table .table-row"), ".table-attribut", ".table-attribut-value");
  	Set<LinkSpec> highlights = getValueOnlySpecs(dom.select(".product-highlights li span"));

  	if (specs == null) return highlights;
  	if (highlights != null) specs.addAll(highlights);

  	return specs;
  }

}
