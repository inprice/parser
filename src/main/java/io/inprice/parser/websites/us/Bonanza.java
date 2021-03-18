package io.inprice.parser.websites.us;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Bonanza USA
 *
 * The data is in two parts: 
 *  - in html body 
 *  - price is handled via a rest call
 *
 *
 * @author mdpinar
 */
public class Bonanza extends AbstractWebsite {

	private Document dom;

	private boolean isAvailable;
  private String sku = Consts.Words.NOT_AVAILABLE;
  private String brand = Consts.Words.NOT_AVAILABLE;
  private List<LinkSpec> specList;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

    Elements specs = dom.select("table.extended_info_table tr.extended_info_row");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String key = spec.selectFirst("td.extended_info_label").text().replaceAll(":", "");
        String value = spec.selectFirst("p.extended_info_value_content").text();
        specList.add(new LinkSpec(key, value));

        if (key.equals("Item number")) sku = value;
        if (key.equals("Brand")) brand = value;

        if (key.equals("Quantity Available")) {
          isAvailable = NumberUtils.toInteger(cleanDigits(value), 0) > 0;
        }
      }
    }
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='og:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return "instock".equals(val.attr("content"));
    }

    return isAvailable;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[property='product:retailer_item_id']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return sku;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = dom.selectFirst("span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    val = dom.selectFirst("div.item_price");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return brand;
  }

  @Override
  public String getSeller() {
    Element val = dom.selectFirst("meta[property='wanelo:store:name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = dom.selectFirst("div.booth_link a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return "Bonanza";
  }

  @Override
  public String getShipment() {
    Element shipment = dom.selectFirst("div.free_shipping");
    if (shipment != null) {
      return "Free shipping";
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return specList;
  }

}
