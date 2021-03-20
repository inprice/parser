package io.inprice.parser.websites.es;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for 101Gigas Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Gigas101 extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='product:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content").trim().contains("instock");
    }
    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[property='product:retailer_part_no']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
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
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    return "101Gigas";
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
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	List<LinkSpec> specList = null;

  	Elements keys = dom.select("div.desc_ci");
  	if (keys != null && keys.size() > 0) {
  		Elements vals = dom.select("div.desc_cd");
  		if (vals != null && vals.size() == keys.size()) {
  			specList = new ArrayList<>(keys.size());
  			for (int i = 0; i < keys.size(); i++) {
  				Element key = keys.get(i);
					Element val = vals.get(i);
					specList.add(new LinkSpec(key.text(), val.text()));
				}
  		}
  	}

  	return specList;
  }

}
