package io.inprice.parser.websites;

import java.math.BigDecimal;
import java.util.List;

import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;

/**
 * A common interface for websites. So, all of them must implement this
 * interface.
 *
 * @author mdpinar
 */
public interface Website {

	// main url
	String getUrl();

	// checks link status and sets data. is implemented in AbstractWebsite
	void check(Link link);

	// indicates the availability of the page
	boolean isAvailable();

	// can be used as SKU, CODE, ASIN, PRODUCT-ID, ITEM-ID...
	String getSku();

	String getName();

	BigDecimal getPrice();

	String getBrand();

	String getSeller();

	String getShipment();

	List<LinkSpec> getSpecList();

}
