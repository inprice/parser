package io.inprice.parser.websites.es;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.de.MediaMarktDE;

/**
 * Parser for Euronics Spain
 *
 * Mostly the same with MediamarktDE class
 *
 * @author mdpinar
 */
public class EuronicsES extends MediaMarktDE {

  @Override
  protected boolean isPageFound() {
  	Element notFoundDiv = dom.selectFirst(".pagError");
		return (notFoundDiv == null);
  }

  @Override
  public String getShipment() {
  	String shipping = "";

  	Element info = dom.selectFirst(".link.active .shippingType-info");
  	Element text = dom.selectFirst(".link.active .shippingType-text");
  	
  	if (info != null) shipping += info.text();
  	if (text != null) shipping += " " + text.text();
  	
    return (StringUtils.isBlank(shipping) ? Consts.Words.NOT_AVAILABLE : shipping);
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("div#more_product_info li"), "span:nth-child(1)", "span:nth-child(2)");
  }

}
