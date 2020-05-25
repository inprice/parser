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
 * Parser for MediaWorld Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaWorld extends AbstractWebsite {

  private Element product;

  public MediaWorld(Link link) {
    super(link);
  }

  @Override
  protected JSONObject getJsonData() {
    product = doc.selectFirst("div.product-detail-main-container");
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    return (product != null && product.attr("data-gtm-avail2").contains("disponibile"));
  }

  @Override
  public String getSku() {
    if (product != null) {
      return product.attr("data-pcode");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (product != null) {
      Element name = product.selectFirst("h1[itemprop='name']");
      if (name != null) {
        StringBuilder sb = new StringBuilder(name.text());
        Element descEL = product.selectFirst("h4.product-short-description");
        if (descEL != null) {
          sb.append(" (");
          sb.append(descEL.text().split("\\|")[0].trim());
          sb.append(")");
        }
        return sb.toString();
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (product != null) {
      return new BigDecimal(cleanDigits(product.attr("data-gtm-price")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Media World";
  }

  @Override
  public String getShipment() {
    Element shipment = doc.selectFirst("p.product-info-shipping");
    if (shipment != null) {
      return shipment.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (product != null) {
      return product.attr("data-gtm-brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getKeyValueSpecList(doc.select("li.content__Tech__row"), "div.Tech-row__inner__key",
        "div.Tech-row__inner__value");
  }
}
