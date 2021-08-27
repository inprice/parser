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

	ParseStatus startParsing(Link link, String html);
	
	boolean isAvailable();

	String getSku();

	String getName();

	BigDecimal getPrice();

	String getBrand();

	String getSeller();

	String getShipment();

	Set<LinkSpec> getSpecs();

}
