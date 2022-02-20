package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * AppliancesOnline, Australia
 * 
 * https://www.appliancesonline.com
 *
 * @author mdpinar
 */
public class AppliancesOnlineAU extends AbstractWebsite {

	private Document dom;
  private String url;

  @Override
  protected String getWaitForSelector() {
  	return "aol-product-specifications-attribute-row";
  	//return ".aol-product-price";
  }

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		
		String title = dom.title();
    if (title.startsWith("Page Not Found") == false) {
    	this.url = link.getUrl();
  		return OK_Status();
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	return (dom.selectFirst(".add-to-cart-button") != null);
  }

  @Override
  public String getSku() {
  	String[] chunks = url.split("-");
    return chunks[chunks.length-1].toUpperCase();
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst(".heading-is-product-title");
    if (val != null) {
    	return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst(".aol-product-price");
    if (val != null) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	String name = getName();
    if (name != null) {
      return name.split("\\s")[0];
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Check delivery and installation info";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select(".specification-item .attribute-row"), ".attribute-name", ".attribute-value");
  }

}
