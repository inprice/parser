package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Kogan, Australia
 *
 * https://www.kogan.com
 *
 * @author mdpinar
 */
public class KoganAU extends AbstractWebsite {

	private Document dom;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().toLowerCase().contains("not found") == false) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    return (dom.getElementById("form-add-to-cart") != null);
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemProp='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    
    val = dom.selectFirst("p[itemProp='model']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    
    val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	Element val = dom.selectFirst("h5[itemProp='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemProp='name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return "Kogan";
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("a[href$='terms-and-conditions']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Element shippingEL = dom.selectFirst("h5[itemProp='price']");
    if (shippingEL != null) {
    	String text = shippingEL.text();
    	if (text.toLowerCase().contains("free shipping")) {
    		return "FREE SHIPPING";
    	}
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getKeyValueSpecs(dom.select("section#specs-accordion dl"), "dt", "dd");
  	
  	if (specs == null || specs.size() == 0) {
  		Elements descPs = dom.select("section[itemprop='description'] ul li");
  		if (CollectionUtils.isNotEmpty(descPs)) {
  			specs = new HashSet<>();
  			for (int i = 0; i < descPs.size(); i++) {
					String key = "";
  				String value = descPs.get(i).text();
  				if (value.indexOf("●") > -1) {
  					String[] features = value.split("●");
  					for (int j = 0; j < features.length; j++) {
  						String val = features[j];
  						if (StringUtils.isBlank(val)) continue;
  						if (val.indexOf(":") > 0) {
  	  					String[] pair = val.split(":");
  	  					key = pair[0];
  	  					val = pair[1];
  	  					specs.add(new LinkSpec(key, val));
  	  				} else {
  	  					specs.add(new LinkSpec("", val));
  	  				}
  					}
  				} else if (value.indexOf(":") > 0) {
  					String[] pair = value.split(":");
  					key = pair[0];
  					value = pair[1];
  					specs.add(new LinkSpec(key, value));
  				} else {
  					specs.add(new LinkSpec("", value));
  				}
				}
  		}
  	}
  	
  	return specs;
  }

}
