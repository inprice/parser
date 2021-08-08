package io.inprice.parser.websites.tr;

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
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Trendyol Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Trendyol extends AbstractWebsite {

	private Document dom;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}
	
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		Element notFoundDiv = dom.getElementById("tydortyuzdortpage");
		if (notFoundDiv == null) {
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst(".add-to-bs, .add-to-basket");
    return (val != null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("link[rel='canonical']");
    if (val != null && StringUtils.isNotBlank(val.attr("href"))) {
      String[] linkChunks = val.attr("href").split("-");
      if (linkChunks.length > 0) {
        return linkChunks[linkChunks.length - 1];
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[name='twitter:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = dom.selectFirst("pr-nm");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[name='twitter:data1']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	Element val = dom.selectFirst(".pr-new-br a");
  	if (val == null) val = dom.selectFirst("div.pr-in-cn div.pr-in-br a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    
    return getSeller();
  }

  @Override
  public String getSeller() {
  	Element val = dom.selectFirst("a.merchant-text");
  	if (val == null) val = dom.selectFirst("span.pr-in-dt-spn");

  	if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("meta[name='twitter:description']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      String[] sellerChunks = val.attr("content").split(":");
      if (sellerChunks.length > 0) {
        return sellerChunks[sellerChunks.length - 1];
      }
    }
    

    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div.stamp.crg div");
    if (val != null) {
      return "Kargo Bedava";
    }

    val = dom.selectFirst("span.pr-in-dt-spn");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().trim() + " tarafından gönderilecektir.";
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specsEl = dom.select("div.pr-in-dt-cn ul span li");
    if (specsEl != null && specsEl.size() > 0) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
        String[] chunks = spec.text().split("\\.");
        for (String sp : chunks) {
          specs.add(new LinkSpec("", sp));
        }
      }
    }

    if (specsEl == null || specsEl.size() == 0) {
    	specsEl = dom.select("li.detail-desc-item");
      if (specsEl != null && specsEl.size() > 0) {
        specs = new HashSet<>();
        for (Element spec : specsEl) {
          String[] chunks = spec.text().split("\\:");
          if (chunks.length > 1) {
            specs.add(new LinkSpec(chunks[0], chunks[1]));
          } else {
          	specs.add(new LinkSpec("", chunks[0]));
          }
        }
      }
    }

    return specs;
  }

}
