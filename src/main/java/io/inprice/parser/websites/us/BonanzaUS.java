package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
 * Bonanza, USA
 *
 * https://www.bonanza.com
 *
 * @author mdpinar
 */
public class BonanzaUS extends AbstractWebsite {

	private Document dom;

	@Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().toLowerCase().contains("find everything") == false) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='product:availability']");
    if (val == null) val = dom.selectFirst("meta[property='og:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content").contains("in_stock") || val.attr("content").contains("instock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[property='product:retailer_item_id']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
  	Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }
  
  @Override
  public String getBrand() {
  	Element val = dom.selectFirst("meta[property='product:brand']");
  	if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
  		return val.attr("content");
  	}
  	return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst(".booth_title_and_feedback .booth_link a");
    if (val != null) {
      return val.text();
    }
    return super.getSeller();
  }

  @Override
  public String getShipment() {
    Elements vals = dom.select(".additional_detail .details_text");
    if (vals != null) {
    	LinkedHashSet<String> set = new LinkedHashSet<>(vals.size());
    	for (int i = 0; i < vals.size(); i++) {
    		Element val = vals.get(i);
    		if (StringUtils.isNotBlank(val.text())) set.add(val.text().replace("Details", "").trim());
			}
    	return String.join(". ", set);
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = null;

    Elements specsEl = dom.select("table.extended_info_table tr.extended_info_row");
    if (CollectionUtils.isNotEmpty(specsEl)) {
      specs = new HashSet<>();
      for (Element spec : specsEl) {
        String key = spec.selectFirst("th.extended_info_label").text().replaceAll(":", "");
        String value = spec.selectFirst("p.extended_info_value_content").text();
        specs.add(new LinkSpec(key, value));
      }
    }

  	return specs;
  }

}
