package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Euronics United Kingdom
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Euronics extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst(".stock-status__message");
    return (val != null && val.text().toLowerCase().indexOf("in stock") > -1);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst(".product-code");
    if (val != null) {
    	return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1.product-name");
    if (val != null) {
    	return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element valUnit = dom.selectFirst(".prd-price__unit");
  	if (valUnit != null) {
  		Element valSubUnit = dom.selectFirst(".prd-price__subunit");
  		if (valSubUnit != null) {
        return new BigDecimal(cleanDigits(valUnit.text() + "." + valSubUnit.text()));
  		}
  		return new BigDecimal(cleanDigits(valUnit.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	Element val = dom.selectFirst("a[href^='/brands'] span");
    if (val != null) {
    	return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	return getKeyValueSpecList(dom.select("table.classifications tr"), "td:nth-child(1)", "td:nth-child(2)");
  }

}
