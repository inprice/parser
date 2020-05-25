package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Euronics Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Euronics extends AbstractWebsite {

  private Element base;

  public Euronics(Link link) {
    super(link);
  }

  @Override
  protected JSONObject getJsonData() {
    base = doc.getElementsByTag("trackingProduct").first();
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    Element availability = doc.selectFirst("span.productDetails__availability.not-available");
    return (availability == null);
  }

  @Override
  public String getSku() {
    if (base != null) {
      return base.attr("productId");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (base != null) {
      return base.attr("productName");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (base != null) {
      return new BigDecimal(cleanDigits(base.attr("price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Euronics";
  }

  @Override
  public String getShipment() {
    Element ship = doc.selectFirst("span.productDetails__label.productDetails__label--left");
    if (ship != null) {
      StringBuilder sb = new StringBuilder();

      sb.append(ship.text());
      sb.append(" ");

      ship = doc.selectFirst("span.productDetails__label.productDetails__label--right");
      if (ship != null) {
        sb.append(ship.text());
      }

      return sb.toString();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (base != null) {
      return base.attr("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("ul.productDetails__specifications li"));
  }
}
