package io.inprice.parser.websites.ca;

import java.math.BigDecimal;
import java.util.Set;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Walmart CA!
 * 
 * This site can sense us, so its status is set as NOT_ALLOWED
 *
 * @author mdpinar
 */
public class Walmart extends AbstractWebsite {

	@Override
	public boolean isAvailable() {
		return true;
	}
	
	@Override
		protected HttpStatus setHtml(String html) {
			return HttpStatus.BLOCKED;
		}

	@Override
	public String getSku() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public BigDecimal getPrice() {
		return BigDecimal.ONE;
	}

	@Override
	public String getBrand() {
		return null;
	}

	@Override
	public String getShipment() {
		return null;
	}

	@Override
	public Set<LinkSpec> getSpecs() {
		return null;
	}

}
