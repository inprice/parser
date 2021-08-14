package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Asos, United Kingdom
 *
 * https://www.asos.com
 *
 * @author mdpinar
 */
public class AsosUK extends AbstractWebsite {

	private Document dom;
  private JSONObject json;
	
  @Override
  protected By waitBy() {
  	return By.className("current-price");
  }
  
	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    String prodData = findAPart(html, "config.product = ", "};", 1);
    if (StringUtils.isNotBlank(prodData)) {
    	json = new JSONObject(prodData);
    	return HttpStatus.OK;
    }
    return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
  	if (json != null && json.has("isInStock")) {
  		return json.getBoolean("isInStock");
  	}
    return false;
  }

  @Override
  public String getSku() {
  	if (json != null && json.has("id")) {
  		return ""+json.getInt("id");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	if (json != null && json.has("name")) {
  		return json.getString("name");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
		Element val = dom.selectFirst(".current-price");

		if (val != null) {
  		return new BigDecimal(cleanDigits(val.text()));
  	}
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	if (json != null && json.has("brandName")) {
  		return json.getString("brandName");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("#shipping-restrictions .shipping-restrictions");
    if (val != null && StringUtils.isNotBlank(val.attr("style"))) {
      String style = val.attr("style");
      if (style != null)
        return "Please refer to Delivery and returns info section";
    }

    val = dom.getElementById("shippingRestrictionsLink");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    } else {
      return "See delivery and returns info";
    }
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return getValueOnlySpecs(dom.select("div.product-description li"));
  }

}
