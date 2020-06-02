package io.inprice.scrapper.worker.websites.es;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;

import org.json.JSONObject;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

/**
 * Parser for Pixmania Spain
 *
 * Please note that: competitor's url (aka main url) is never used for data pulling
 *
 * This is a very special case. Pixmania actually provides nothing in html form.
 * So, we need to make two different request to collect all the data we need. 
 *    a) The first is for token info pulled by product-id, whose steps are explained below 
 *    b) The second is for the data 
 * Besides, in order to make the first request we also need to get product-id,  which is in place the main url
 *
 * Product-id 
 *  - is derived from url by splitting it up by dash - 
 *  - the last part of url is the product-id
 *  - in getJsonData(), the data is pulled with the payload whose steps are explained above 
 *  - all data is built by using bestOffer and json object. the two are set in getJsonData()
 *
 * @author mdpinar
 */
public class Pixmania extends AbstractWebsite {

  /*
   * the main data derived from json gotten server via getSubUrl()
   */
  private JSONObject bestOffer;

  public Pixmania(Competitor competitor) {
    super(competitor);
  }

  /**
   * This url is substitute for competitor's original url Since Pixmania provides
   * nothing in html form
   *
   * @return String - the reference url
   */
  @Override
  protected String getAlternativeUrl() {
    return "https://www.pixmania.es/api/ecrm/session";
  }

  /**
   * Returns product-id which is extracted from main url by splitting it up by
   * dash (the last part we are looking for)
   *
   * @return String - product-id
   */
  private String findProductId() {
    final String[] urlChunks = getUrl().split("\\?");
    if (urlChunks.length > 0) {
      final String[] partChunks = urlChunks[0].split("-");
      if (partChunks.length > 0) {
        return partChunks[partChunks.length - 1];
      }
    }
    return null;
  }

  /**
   * Returns token taken place in html body as json format
   *
   * @return String - token derived from html body
   */
  private String getToken() {
    if (doc != null) {
      JSONObject tokenData = new JSONObject(doc.body().text());
      if (tokenData.has("session")) {
        return tokenData.getJSONObject("session").getString("token");
      }
    }
    return null;
  }

  /**
   * Returns payload as key value maps
   *
   * @return Map - payload required for having the data
   */
  public Map<String, String> getPayload() {
    String token = getToken();
    if (token != null) {
      Map<String, String> payload = new HashMap<>();
      payload.put("Authorization", "Bearer " + token);
      payload.put("Language", "es-ES");
      return payload;
    }
    return null;
  }

  /**
   * Request the data with a constant url with product-id and payload. Besides,
   * data handles best-offer which holds some important data
   *
   * @return JSONObject - json
   */
  @Override
  public JSONObject getJsonData() {
    final String productId = findProductId();
    if (productId != null) {

      final Map<String, String> payload = getPayload();
      if (payload != null) {

        HttpResponse<String> response = httpClient
            .get(String.format("https://www.pixmania.es/api/pcm/products/%s", productId), payload);
        if (response != null && response.getStatus() < 400) {
          JSONObject data = new JSONObject(response.getBody());
          if (data.has("product")) {
            JSONObject productEL = data.getJSONObject("product");
            if (productEL.has("best_offer")) {
              bestOffer = productEL.getJSONObject("best_offer");
            }
            return productEL;
          }
        }
      }
    }
    return null;
  }

  @Override
  public boolean isAvailable() {
    if (bestOffer != null && bestOffer.has("stock")) {
      return bestOffer.getInt("stock") > 0;
    }
    return false;
  }

  @Override
  public String getSku() {
    if (bestOffer != null && bestOffer.has("sku")) {
      return bestOffer.getString("sku");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
      return json.getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (bestOffer != null && bestOffer.has("price_with_vat")) {
      return bestOffer.getBigDecimal("price_with_vat");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    if (bestOffer != null && bestOffer.has("merchant")) {
      JSONObject merchant = bestOffer.getJSONObject("merchant");
      if (merchant.has("name")) {
        return merchant.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    if (bestOffer != null && bestOffer.has("free_shipping")) {
      return "Free shipping: " + bestOffer.getBoolean("free_shipping");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      JSONObject merchant = json.getJSONObject("brand");
      if (merchant.has("name")) {
        return merchant.getString("name");
      }
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    List<CompetitorSpec> specList = null;

    if (json != null && json.has("description")) {
      final String description = json.getString("description");
      final String[] descChunks = description.split("-");
      if (descChunks.length > 0) {
        specList = new ArrayList<>();
        for (String desc : descChunks) {
          specList.add(new CompetitorSpec("", desc));
        }
      }
    }
    return specList;
  }

}
