package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for MediaMarkt Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaMarkt extends AbstractWebsite {

  private BigDecimal freeShippingTresholdForNL;

  public MediaMarkt(Link link) {
    super(link);
  }

  @Override
  protected JSONObject getJsonData() {
    final String tresholdForNL = findAPart(doc.html(), "bezorgkostenDrempel =", ";");

    if (tresholdForNL != null) {
      freeShippingTresholdForNL = new BigDecimal(cleanDigits(tresholdForNL));
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    Element available = doc.selectFirst(".online-nostock");
    if (available != null)
      return false;

    available = doc.selectFirst("meta[property='og:availability']");
    if (available != null) {
      return !available.attr("content").trim().equals("out of stock");
    }

    return false;
  }

  @Override
  public String getSku() {
    Element sku = doc.selectFirst("dd span[itemprop='sku']");
    if (sku != null) {
      return sku.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element name = doc.selectFirst("meta[property='og:title']");
    if (name != null) {
      return name.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element price = doc.selectFirst("meta[property='product:price:amount']");
    if (price != null) {
      return new BigDecimal(cleanDigits(price.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element seller = doc.selectFirst("meta[property='og:site_name']");
    if (seller != null) {
      return seller.attr("content");
    }
    return "Media Markt";
  }

  @Override
  public String getShipment() {
    if (freeShippingTresholdForNL != null) {
      if (freeShippingTresholdForNL.compareTo(getPrice()) > 0) {
        return "Gratis bezorging vanaf € " + freeShippingTresholdForNL.toString();
      } else {
        return "Gratis bezorging";
      }
    }

    Element shipment = doc.selectFirst("div.price.big");
    if (shipment != null) {
      Element desc = shipment.nextElementSibling().selectFirst("small");
      if (desc != null)
        return desc.text();
    }

    shipment = doc.selectFirst("div.old-price-block");
    if (shipment != null) {
      Element desc = shipment.nextElementSibling().selectFirst("small");
      if (desc != null)
        return desc.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element brand = doc.selectFirst("meta[property='product:brand']");
    if (brand != null) {
      return brand.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    String parentClass = "specification";
    Elements isParentExist = doc.select("dl." + parentClass);
    if (isParentExist == null)
      parentClass = "product-details";

    Elements specKeys = doc.select(String.format("dl.%s dt", parentClass));
    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      for (Element key : specKeys) {
        specList.add(new LinkSpec(key.text().replaceAll(":", ""), ""));
      }
    }

    Elements specValues = doc.select(String.format("dl.%s dd", parentClass));
    if (specValues != null && specValues.size() > 0) {
      boolean isEmpty = false;
      if (specList == null) {
        isEmpty = true;
        specList = new ArrayList<>();
      }
      for (int i = 0; i < specList.size(); i++) {
        Element value = specValues.get(i);
        if (isEmpty) {
          specList.add(new LinkSpec("", value.text()));
        } else {
          specList.get(i).setValue(value.text());
        }
      }
    }

    if (specList == null) {
      specValues = doc.select("p.autoWrapParagraph p");
      if (specValues != null && specValues.size() > 0) {
        specList = new ArrayList<>();
        for (Element spec : specValues) {
          specList.add(new LinkSpec("", spec.text()));
        }
      }
    }

    return specList;
  }
}
