package io.inprice.parser.websites.zz;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Test parser which can be used for only test purposes
 *
 * @author mdpinar
 */
public class Sanal extends AbstractWebsite {

	private Document dom;

	@Override
	protected Renderer getRenderer() {
		return Renderer.JSOUP;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		return OK_Status();
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.getElementById("available");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return Boolean.parseBoolean(val.text().trim());
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.getElementById("sku");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.getElementById("name");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.getElementById("price");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = dom.getElementById("seller");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.getElementById("shipment");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = dom.getElementById("brand");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

	@Override
	public Set<LinkSpec> getSpecs() {
		return null;
	}
  
}