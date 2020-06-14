package io.inprice.scrapper.worker.websites.ca;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kong.unirest.HttpResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

/**
 * SPA based parser for Walmart Canada Please note that: Since website is a kind
 * of SPA page, its url is never directly used!
 *
 * The parsing steps:
 *
 * - we need a payload to get the data - in order to do that we need two info:
 * a) product-id b) sku
 *
 * in getPayload(), that payload is generated by using product-id and sku
 *
 * Product-id - is derived from url by splitting it up by forward slash / - the
 * last part of url is the product-id SKU - the html body of competitor's url contains
 * sku data as json
 *
 * - in getJsonData(), the data is pulled with the payload whose steps are
 * explained above - all data is built by using html body, preData set in
 * getPayload(), and json object set in getJsonData()
 *
 * @author mdpinar
 */
public class Walmart extends AbstractWebsite {

  // used in pre-request
  private static final String STATIC_DATA = "{'availabilityStoreId':'3124','fsa':'P7B','lang':'en','products':[{'productId':'%s','skuIds':['%s']}]}";

  // used for getting the data
  private static final String STATIC_URL = "https://www.walmart.ca/api/product-page/price-offer";

  /*
   * the main data derived from json placed in html
   */
  private JSONObject preData;

  private String sku;

  public Walmart(Competitor competitor) {
    super(competitor);
  }

  /**
   * In order to generate the payload to request the data; the method uses html
   * body for extracting sku, and competitor's url for product id
   *
   * @return String - a competitor for the data pulling from the server
   */
  public String getPayload() {
    if (doc != null) {

      final String skus = findAPart(doc.html(), "\"skus\":[\"", "]", -1);

      if (skus != null) {
        Element preDataEL = doc.selectFirst("div.js-content script[type='application/ld+json']");
        if (preDataEL != null) {
          preData = new JSONObject(preDataEL.dataNodes().get(0).getWholeData());
          if (preData.has("sku")) {
            sku = preData.getString("sku");
            String[] urlChunks = getUrl().split("/");
            if (urlChunks.length > 0) {
              return String.format(STATIC_DATA, urlChunks[urlChunks.length - 1], skus).replaceAll("'", "\"");
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Request the data with a constant url with payload
   *
   * @return JSONObject - json
   */
  @Override
  public JSONObject getJsonData() {
    CompetitorStatus preStatus = getCompetitorStatus();
    setCompetitorStatus(CompetitorStatus.NO_DATA);

    final String payload = getPayload();

    if (payload != null && StringUtils.isNotBlank(payload)) {
      HttpResponse<String> response = httpClient.post(STATIC_URL, payload);
      if (response != null && response.getStatus() > 0 && response.getStatus() < 400) {
        JSONObject product = new JSONObject(response.getBody());

        if (product.has("skus") && product.has("offers")) {
          JSONObject skus = product.getJSONObject("skus");
          JSONObject offers = product.getJSONObject("offers");

          setCompetitorStatus(preStatus);

          if (!skus.isEmpty()) {
            for (String s : skus.keySet()) {
              JSONArray hashArray = skus.getJSONArray(s);
              if (hashArray.length() > 0) {
                for (int i = 0; i < hashArray.length(); i++) {
                  JSONObject offer = offers.getJSONObject(hashArray.getString(i));
                  if (!offer.isEmpty()) {
                    if ("Available".equals(offer.getString("gmAvailability"))) {
                      return offer;
                    }
                  }
                }
              }
            }
          }
        } else {
          log.error("Failed to fetch data! CompetitorStatus: NO_DATA");
        }
      } else {
        setCompetitorStatus(response);
      }
    } else {
      log.error("Failed to create payload, NO_DATA");
      setCompetitorStatus(CompetitorStatus.READ_ERROR);
    }

    return null;
  }

  @Override
  public boolean isAvailable() {
    if (json != null && json.has("gmAvailability")) {
      return "Available".equals(json.getString("gmAvailability"));
    }
    return false;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public String getName() {
    if (preData != null && preData.has("name")) {
      return preData.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (json != null && json.has("currentPrice")) {
      return json.getBigDecimal("currentPrice");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (json != null && json.has("sellerInfo")) {
      JSONObject sellerInfo = json.getJSONObject("sellerInfo");
      if (sellerInfo.has("en"))
        return sellerInfo.getString("en");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    if (getSeller() != null) {
      return "Sold & shipped by " + getSeller();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (preData != null && preData.has("brand")) {
      JSONObject brand = preData.getJSONObject("brand");
      if (brand.has("name")) {
        return brand.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    final String features = findAPart(doc.html(), "featuresSpecifications\":\"", "\",\"type\"");

    List<CompetitorSpec> specList = null;

    if (features != null) {
      String[] specs = features.split("•");
      if (specs.length > 0) {
        specList = new ArrayList<>();
        for (String spec : specs) {
          if (StringUtils.isNotBlank(spec)) {
            final String clean = spec.replaceAll(".u2028|.u003C", "").replaceAll("br>", "");
            specList.add(new CompetitorSpec("", clean));
          }
        }
      }
    }
    return specList;
  }
}
