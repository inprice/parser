package io.inprice.parser.websites.it;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for EPrice Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class EPrice extends AbstractWebsite {

	private Document dom;
	private String seller;
	
	@Override
	protected void setHtml(String html) {
		
		dom = Jsoup.parse(html);
		seller = findAPart(html, "seller_name: \"", "\",");
		if (StringUtils.isBlank(seller)) seller = "ePrice";
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[itemprop='availability']");
    if (val != null) {
      return val.attr("content").contains("InStock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    String val = dom.title();
  	if (val.indexOf("-") > 0) {
  		return val.split("-")[0];
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    return seller;
  }

  @Override
  public String getShipment() {
    return "Venduto e spedito da " + getSeller();
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specsEl = dom.select("#anchorCar li");
    if (specsEl != null && specsEl.size() > 0) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
        String liHtml = spec.html();
        if (liHtml.contains("<span>")) {
        	liHtml = liHtml.replace("<span>", "").replace("</span>", "ç");
        }
        String[] pair = liHtml.split("ç");
        specs.add(new LinkSpec(pair[0], pair[1]));
      }
    }

    if (specs == null) {
      specsEl = dom.select("#anchorDesc p");
      if (specsEl != null && specsEl.size() > 0) {
        specs = new HashSet<>();
        for (Element spec : specsEl) {
          String[] specChunks = spec.text().split("\\.");
          for (String sp : specChunks) {
            specs.add(new LinkSpec("", sp));
          }
        }
      }
    }
    
    if (specs == null) {
    	specs = getKeyValueSpecs(dom.select("#anchorCar li"), "span", "a");
    }

    return specs;
  }

}
