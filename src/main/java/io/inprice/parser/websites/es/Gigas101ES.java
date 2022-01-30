package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.util.Set;

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
 * 101Gigas, Spain
 *
 * https://101gigas.com
 *
 * @author mdpinar
 */
public class Gigas101ES extends AbstractWebsite {

	private Document dom;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);

		Element titleEl = dom.selectFirst("title");
		if (titleEl.text().contains("404") == false) {
			return OK_Status();
		}
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='product:availability']");
    if (val != null && val.hasAttr("content")) {
      String href = val.attr("content").toLowerCase();
      return href.contains("instock") || href.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[property='product:retailer_part_no']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='product:sale_price:amount']");
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
  public String getShipment() {
    Elements vals = dom.select("div.availability div#codigosku");
    if (vals != null && !vals.isEmpty()) {
      for (int i = 0; i < vals.size(); i++) {
        Element note = vals.get(i);
        if (note.text().contains("Envío")) {
          return note.text();
        }
      }
    }
    return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	Set<LinkSpec> specs = getFlatKeyValueSpecs(dom.select("#desc_prop div.desc_ci"), dom.select("#desc_prop div.desc_cd"));
  	if (specs == null) specs = getKeyValueSpecs(dom.select("table.tabla_caracteristicas tr"), "td:nth-child(1)", "td:nth-child(2)");

  	return specs;
  }

}
