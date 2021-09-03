package io.inprice.parser.websites.it;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * EPrice, Italy
 *
 * Default status of this parser is BLOCKED because doesn't allow to scrape!
 * 
 * https://www.eprice.it
 * 
 * Maybe it can be scrapped with a different call
 * 
 * for this url --> https://www.eprice.it/coltelli-da-cucina-Beper/d-51659976
 * this         --> https://spep.eprice.it/Sperest.svc//checkMethod?method=GetPromoForSku&parameters=({"IdListino":2,"sku":"51659976","IdVertical":"0","EscludiMarketPlace":false})
 *
 * @author mdpinar
 */
public class EPriceIT extends AbstractWebsite {

	private Document dom;

	private String seller;
	
	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		seller = findAPart(html, "seller_name: \"", "\",");
		if (StringUtils.isBlank(seller)) seller = super.getSeller();

		return OK_Status();
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[itemprop='availability']");
    if (val != null && val.hasAttr("content")) {
      String href = val.attr("content").toLowerCase();
      return href.contains("instock") || href.contains("preorder");
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
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    return "Venduto e spedito da " + seller;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specsEl = dom.select("#anchorCar li");
    if (CollectionUtils.isNotEmpty(specsEl)) {
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
      if (CollectionUtils.isNotEmpty(specsEl)) {
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
