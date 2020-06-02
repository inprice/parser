package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
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
 * Parser for MediaMarkt Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaMarkt extends AbstractWebsite {

  private BigDecimal freeShippingTresholdForNL;

  public MediaMarkt(Competitor competitor) {
    super(competitor);
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
    Element val = doc.selectFirst(".online-nostock");
    if (val != null) {
      return false;
    }

    val = doc.selectFirst("meta[property='og:availability']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return !val.attr("content").trim().equals("out of stock");
    }

    return false;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("dd span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = doc.selectFirst("meta[property='product:price:amount']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("meta[property='og:site_name']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
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
      Element val = shipment.nextElementSibling().selectFirst("small");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    shipment = doc.selectFirst("div.old-price-block");
    if (shipment != null) {
      Element val = shipment.nextElementSibling().selectFirst("small");
      if (val != null && StringUtils.isNotBlank(val.text())) {
        return val.text();
      }
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("meta[property='product:brand']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    String parentClass = "specification";
    Elements isParentExist = doc.select("dl." + parentClass);
    if (isParentExist == null)
      parentClass = "product-details";

    Elements specKeys = doc.select(String.format("dl.%s dt", parentClass));
    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      for (Element key : specKeys) {
        specList.add(new CompetitorSpec(key.text().replaceAll(":", ""), ""));
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
          specList.add(new CompetitorSpec("", value.text()));
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
          specList.add(new CompetitorSpec("", spec.text()));
        }
      }
    }

    return specList;
  }
}
