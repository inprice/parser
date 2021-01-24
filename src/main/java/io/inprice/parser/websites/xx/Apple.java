package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;
import kong.unirest.HttpResponse;

/**
 * Parser for Apple Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public abstract class Apple extends AbstractWebsite {

  private static final String REFERRER = "https://www.apple.com/au/shop/buy-ipad/ipad-pro";

  private JSONObject offers;
  private JSONObject product;
  private boolean available;

  private String testCountry;

  public Apple() {
    super();
  }

  public Apple(String testCountry) {
    this.testCountry = testCountry;
  }

  @Override
  public JSONObject getJsonData() {
    LinkStatus preStatus = getLinkStatus();
    setLinkStatus(LinkStatus.NO_DATA);

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
        setLinkStatus("Offers json not found");
        log.error("Failed to fetch data! Offers json not found");
      }
      return data;
    } else {
      setLinkStatus("Application json data not found");
      log.error("Failed to fetch data! Application json data not found");
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
    if (isAvailable()) {
      if (offers != null && offers.has("price")) {
        return offers.getBigDecimal("price");
      }
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

  @Override
  protected Link getTestLink() {
    return new Link(String.format("https://www.apple.com/%s/shop/", this.testCountry));
  }
  
  @Override
	public String getSiteName() {
		return "apple";
	}

}
