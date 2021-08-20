package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
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
 * Bonprix, Global
 *
 * https://www.bonprix.com
 *
 * @author mdpinar
 */
public class BonprixXX extends AbstractWebsite {

	private Document dom;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);
		
		Element notFoundDiv = dom.selectFirst(".errorpage-teaser-text");
		if (notFoundDiv == null) {
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='og:availability']");
    if (val == null) val = dom.selectFirst("meta[content='https://schema.org/InStock']");

    if (val != null && val.hasAttr("content")) {
      String href = val.attr("content").toLowerCase();
      return href.contains("instock") || href.contains("preorder");
    }
    
    val = dom.selectFirst("div.product-availability-box_wrapper div");
    if (val == null) dom.selectFirst("div#product-detail-availibility-container noscript");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().contains("erfügbar");
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
    Element val = dom.selectFirst("h1.product-name span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("span.price");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    val = dom.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    val = dom.selectFirst("span.clearfix.price");
    if (val == null) val = dom.selectFirst("meta[property='og:price:amount']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val == null) val = dom.selectFirst("meta[property='og:brand']");
    if (val == null) val = dom.selectFirst(".product-brand span");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div[data-controller='infoicon'] div.info-text");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll(" ada12_info-icon-bigsize-additional-text", "");
    }

    val = dom.selectFirst("div.product-availability-box_wrapper div");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.getElementById("aiDelChargeSame");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specKeys = dom.select("div.product-attributes strong");
    if (CollectionUtils.isNotEmpty(specKeys)) {
      specs = new HashSet<>();
      Elements specValues = dom.select("div.product-attributes span");
      for (int i = 0; i < specKeys.size(); i++) {
        Element key = specKeys.get(i);
        Element val = null;
        if (i < specValues.size() - 1) {
          val = specValues.get(i);
        }
        specs.add(new LinkSpec(key.text().replaceAll(":", ""), (val != null ? val.text() : "")));
      }
      return specs;
    }

    specKeys = dom.select("div.productFeaturesContainer span.productFeatureName");
    if (specKeys == null)
      specKeys = dom.select("div.product-attributes strong");

    if (CollectionUtils.isNotEmpty(specKeys)) {
      specs = new HashSet<>();
      for (Element key : specKeys) {
        specs.add(new LinkSpec(key.text().replaceAll(":", ""), ""));
      }
    }

    Elements specValues = dom.select("div.productFeaturesContainer span.productFeatureValue");

    if (CollectionUtils.isNotEmpty(specValues)) {
      boolean isEmpty = false;
      if (specs == null) {
        isEmpty = true;
        specs = new HashSet<>();
      }
      for (int i = 0; i < specValues.size(); i++) {
        Element value = specValues.get(i);
        if (isEmpty) {
          specs.add(new LinkSpec("", value.text()));
        //} else {
        //  specs.get(i).setValue(value.text());
        }
      }

      return specs;
    }

    specValues = dom.select("div.productDescription");

    if (specValues != null) {
      specs = new HashSet<>();
      String desc = specValues.text();
      String[] descChunks = desc.split("\\.");
      if (descChunks.length > 0) {
        for (String dsc : descChunks) {
          specs.add(new LinkSpec("", dsc));
        }
      }
    }

    return specs;
  }

}
