package io.inprice.scrapper.worker.websites.xx;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Apple Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Apple extends AbstractWebsite {

  private static final String REFERRER = "https://www.apple.com/au/shop/buy-ipad/ipad-pro";

  private JSONObject offers;
  private JSONObject product;
  private boolean available;

  public Apple(Link link) {
    super(link);
  }

  @Override
  public JSONObject getJsonData() {
    Status preStatus = getLinkStatus();
    setLinkStatus(Status.NO_DATA);

    Element dataEL = doc.selectFirst("script[type='application/ld+json']");
    if (dataEL != null) {
      JSONObject data = new JSONObject(dataEL.dataNodes().get(0).getWholeData());
      if (data.has("offers")) {
        JSONArray offersArray = data.getJSONArray("offers");
        if (!offersArray.isEmpty()) {
          offers = offersArray.getJSONObject(0);
          if (offers != null && offers.has("sku")) {
            final int index = getUrl().indexOf("/shop/");

            if (index > 0) {
              final String sku = offers.getString("sku");
              final String rootDomain = getUrl().substring(0, index);

              HttpResponse<String> response = httpClient.get(rootDomain + "/shop/delivery-message?parts.0=" + sku,
                  REFERRER);
              if (response != null && response.getStatus() > 0 && response.getStatus() < 400) {
                if (response.getBody() != null && !response.getBody().trim().isEmpty()) {
                  JSONObject shipment = new JSONObject(response.getBody());
                  if (!shipment.isEmpty()) {
                    if (shipment.getJSONObject("body").getJSONObject("content").getJSONObject("deliveryMessage")
                        .has(sku)) {
                      product = shipment.getJSONObject("body").getJSONObject("content").getJSONObject("deliveryMessage")
                          .getJSONObject(sku);
                      available = product.getBoolean("isBuyable");
                      setLinkStatus(preStatus);
                    }
                  }
                }
              } else {
                setLinkStatus(response);
              }
            }
          }
        }
      } else {
        log.error("Failed to fetch data! Status: READ_ERROR --> L1");
        setLinkStatus(Status.READ_ERROR);
      }
      return data;
    } else {
      log.error("Failed to fetch data! Status: READ_ERROR --> L2");
      setLinkStatus(Status.READ_ERROR);
    }
    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      return offers.getString("availability").contains("InStock");
    }

    Element availability = doc.getElementById("configuration-form");
    if (availability != null) {
      String data = availability.attr("data-eVar20");
      return data.contains("In Stock");
    }
    return available;
  }

  @Override
  public String getSku() {
    if (offers != null && offers.has("sku")) {
      return offers.getString("sku");
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
    if (offers != null && offers.has("price")) {
      return offers.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Apple";
  }

  @Override
  public String getShipment() {
    if (product != null) {
      if (product.has("promoMessage")) {
        return product.getString("promoMessage");
      }
      JSONArray options = product.getJSONArray("deliveryOptions");
      if (options.length() > 0) {
        JSONObject delivery = options.getJSONObject(0);
        StringBuilder sb = new StringBuilder();
        if (delivery.has("displayName"))
          sb.append(delivery.getString("displayName")).append(": ");
        if (delivery.has("shippingCost"))
          sb.append(delivery.getString("shippingCost"));
        return sb.toString();
      }
    }
    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      JSONObject brand = json.getJSONObject("brand");
      if (brand.has("name")) {
        return brand.getString("name");
      }
    }
    return "Apple";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.as-productinfosection-mainpanel div.para-list"));
  }

}
