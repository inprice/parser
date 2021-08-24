package io.inprice.parser.websites;

import java.math.BigDecimal;
import java.util.Set;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;

/**
 * A common interface for websites. So, all of them must implement this
 * interface.
 *
 * @author mdpinar
 */
public interface Website {

	// checks link status and sets data. is implemented in AbstractWebsite
	ParseStatus check(Link link);

	// indicates the availability of the page
	boolean isAvailable();

	// can be used as SKU, CODE, ASIN, PRODUCT-ID, ITEM-ID...
	String getSku(String url);

	String getName();

	BigDecimal getPrice();

	String getBrand();

	String getSeller(String defaultName);

	String getShipment();

	Set<LinkSpec> getSpecs();

}
