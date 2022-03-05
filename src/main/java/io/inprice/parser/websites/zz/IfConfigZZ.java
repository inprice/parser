package io.inprice.parser.websites.zz;

import java.math.BigDecimal;
import java.util.Set;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

public class IfConfigZZ extends AbstractWebsite {

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}
	
	@Override
	public ParseStatus startParsing(Link link, String html) {
		System.out.println(html);
		return OK_Status();
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public String getSku() {
		return GlobalConsts.NOT_AVAILABLE;
	}

	@Override
	public String getName() {
		return GlobalConsts.NOT_AVAILABLE;
	}

	@Override
	public BigDecimal getPrice() {
		return BigDecimal.TEN;
	}

	@Override
	public String getBrand() {
		return GlobalConsts.NOT_AVAILABLE;
	}

	@Override
	public String getShipment() {
		return GlobalConsts.NOT_AVAILABLE;
	}

	@Override
	public Set<LinkSpec> getSpecs() {
		return null;
	}

}
