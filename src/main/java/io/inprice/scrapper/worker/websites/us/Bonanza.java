package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

  /*
   * The following data can only be gathered over spec list
   */
  private String sku = "NA";
  private String brand = "NA";
  private boolean availability;
  private List<LinkSpec> specList;

  public Bonanza(Link link) {
    super(link);
  }

  /**
   * This method is used as a initial data loader using product's spec list. Class
   * level variables are set over the spec list here.
   *
   * @return nothing!
   */
  @Override
  protected JSONObject getJsonData() {
    Elements specs = doc.select("table.extended_info_table tr.extended_info_row");
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        String key = spec.selectFirst("td.extended_info_label").text().replaceAll(":", "");
        String value = spec.selectFirst("p.extended_info_value_content").text();
        specList.add(new LinkSpec(key, value));

        if (key.equals("Item number"))
          sku = value;
        if (key.equals("Brand"))
          brand = value;

        if (key.equals("Quantity Available")) {
          try {
            availability = (new Integer(cleanDigits(value))) > 0;
          } catch (Exception e) {
            //
          }
        }
      }
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    Element val = doc.selectFirst("meta[property='og:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return "instock".equals(val.attr("content"));
    }

    return availability;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("meta[property='product:retailer_item_id']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return sku;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = doc.selectFirst("span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    val = doc.selectFirst("div.item_price");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("meta[property='wanelo:store:name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = doc.selectFirst("div.booth_link a");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return "Bonanza";
  }

  @Override
  public String getShipment() {
    Element shipment = doc.selectFirst("div.free_shipping");
    if (shipment != null) {
      return "Free shipping";
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return brand;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return specList;
  }
}
