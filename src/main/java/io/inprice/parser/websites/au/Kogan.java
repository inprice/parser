package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Kogan Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Kogan extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
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
    
    return Consts.Words.NOT_AVAILABLE;
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
    
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    
    val = dom.selectFirst("h5[itemProp='price']");
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
    return "Kogan.com";
  }

  @Override
  public String getShipment() {
    Element shippingEL = dom.selectFirst("h5[itemProp='price']");
    if (shippingEL != null) {
    	String text = shippingEL.text();
    	if (text.toLowerCase().contains("free shipping")) {
    		return "FREE SHIPPING";
    	} else {
    		return "SEE THE CONDITIONS";
    	}
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	List<LinkSpec> specList = getKeyValueSpecList(dom.select("section#specs-accordion dl"), "dt", "dd");
  	
  	if (specList == null || specList.size() == 0) {
  		Elements descPs = dom.select("section[itemprop='description'] p");
  		if (descPs != null && descPs.size() > 0) {
  			specList = new ArrayList<>();
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
  	  					specList.add(new LinkSpec(key, val));
  	  				} else {
  	  					specList.add(new LinkSpec("", val));
  	  				}
  					}
  				} else if (value.indexOf(":") > 0) {
  					String[] pair = value.split(":");
  					key = pair[0];
  					value = pair[1];
  					specList.add(new LinkSpec(key, value));
  				} else {
  					specList.add(new LinkSpec("", value));
  				}
				}
  		}
  	}
  	
  	return specList;
  }

}
