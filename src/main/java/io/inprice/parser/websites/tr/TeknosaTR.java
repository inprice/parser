package io.inprice.parser.websites.tr;

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
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Teknosa, Turkiye
 *
 * https://www.teknosa.com
 *
 * @author mdpinar
 */
public class TeknosaTR extends AbstractWebsite {

	private Document dom;
	private Element addToCartBtn;
	
	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		addToCartBtn = dom.getElementById("addToCartButton");
		if (addToCartBtn != null) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	return addToCartBtn.attr("data-product-stock").equalsIgnoreCase("Y");
  }

  @Override
  public String getSku() {
  	return addToCartBtn.attr("data-product-id");
  }

  @Override
  public String getName() {
  	return addToCartBtn.attr("data-product-name");
  }

  @Override
  public BigDecimal getPrice() {
  	String val = addToCartBtn.attr("data-product-price");
    if (StringUtils.isBlank(val)) {
    	val = addToCartBtn.attr("data-product-discounted-price");
    	if (StringUtils.isBlank(val)) {
    		val = addToCartBtn.attr("data-product-actual-price");
    	}
    }
      
    if (StringUtils.isNotBlank(val)) {
    	return new BigDecimal(cleanDigits(val));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div.pw-dangerous-html.dbh-content");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.getElementById("hd3");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return "Mağazada teslim";
  }

  @Override
  public String getBrand() {
  	return addToCartBtn.attr("data-product-brand");
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specKeys = dom.select("div.product-classifications tr");
    if (CollectionUtils.isNotEmpty(specKeys)) {
      specs = new HashSet<>();
      for (Element key : specKeys) {
        Elements vals = key.select("td");
        if (vals.size() > 0) {
        	if (vals.size() == 1) {
        		specs.add(new LinkSpec(vals.get(0).text(), ""));
        	} else {
        		specs.add(new LinkSpec(vals.get(0).text(), vals.get(1).text()));
        	}
        }
      }
    }

    Elements specValues = dom.select("div.product-classifications tr");
    if (CollectionUtils.isNotEmpty(specValues)) {
      boolean isEmpty = false;
      if (specs == null) {
        isEmpty = true;
        specs = new HashSet<>();
      }
      for (int i = 0; i < specValues.size(); i++) {
        Element value = specValues.get(i);
        Element val = value.selectFirst("td span");
        if (isEmpty) {
          specs.add(new LinkSpec("", val.text()));
        //} else {
        //  specs.get(i).setValue(val.text());
        }
      }
    }

    return specs;
  }

}
