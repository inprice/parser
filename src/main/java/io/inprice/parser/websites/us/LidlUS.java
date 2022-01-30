package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Lidl, USA
 * 
 * https://www.lidl.com
 *
 * @author mdpinar
 */
public class LidlUS extends AbstractWebsite {

	private Document dom;

	private String url;

	@Override
	protected By waitBy() {
		return By.className("product-price");
	}
	
	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element messageH1 = dom.selectFirst(".status-message-headline");
		if (messageH1 == null) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	Element val = dom.selectFirst(".instock");
    if (val != null) return true;

    val = dom.selectFirst(".stock-status-stock");
    return (val != null && val.text().toLowerCase().contains("in stock"));
  }

  @Override
  public String getSku() {
    String[] chunks = url.split("/");
    if (chunks.length > 0) {
      return chunks[chunks.length-1];
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst(".product-title");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst(".product-price .price");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	String[] chunks = getName().split("®");
    if (chunks.length > 1) {
      return chunks[0];
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst(".stock-status-store");
    if (val != null) {
    	return "Available at " + val.text();
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select(".description li"));
  }

}
