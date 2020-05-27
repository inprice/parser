package io.inprice.scrapper.worker.websites.uk;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.LinkStatus;
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
 * Parser for Asos UK
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Asos extends AbstractWebsite {

  private BigDecimal price = BigDecimal.ZERO;
  private boolean isAvailable;

  public Asos(Link link) {
    super(link);
  }

  @Override
  protected JSONObject getJsonData() {
    LinkStatus preStatus = getLinkStatus();
    setLinkStatus(LinkStatus.NO_DATA);

    final String sku = getSku();

    HttpResponse<String> response = httpClient
        .get("https://www.asos.com/api/product/catalogue/v3/stockprice?currency=EUR&store=ROW&productIds=" + sku);
    if (response != null && response.getStatus() > 0 && response.getStatus() < 400) {

      if (response.getBody() != null && !response.getBody().trim().isEmpty()) {
        JSONArray items = new JSONArray(response.getBody());
        if (items.length() > 0) {
          JSONObject parent = items.getJSONObject(0);

          if (parent.has("productPrice")) {
            JSONObject pprice = parent.getJSONObject("productPrice");
            price = pprice.getJSONObject("current").getBigDecimal("value");
            setLinkStatus(preStatus);
          }

          if (parent.has("variants")) {
            JSONArray variants = parent.getJSONArray("variants");
            if (variants.length() > 0) {
              for (int i = 0; i < variants.length(); i++) {
                JSONObject var = variants.getJSONObject(i);
                if (var.getBoolean("isInStock")) {
                  isAvailable = true;
                  break;
                }
              }
            }
          }
        }
      } else {
        log.error("Failed to fetch data! Status: READ_ERROR");
        setLinkStatus(LinkStatus.NO_DATA);
      }
    } else {
      setLinkStatus(response);
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    Element sku = doc.selectFirst("span[itemprop='sku']");
    if (sku != null) {
      return sku.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element name = doc.selectFirst("div.product-hero h1");
    if (name != null) {
      return name.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public String getSeller() {
    Element seller = doc.selectFirst("span[itemprop='seller'] span[itemprop='name']");
    if (seller != null) {
      return seller.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element shipment = doc.selectFirst("#shipping-restrictions .shipping-restrictions");
    if (shipment != null) {
      String val = shipment.attr("style");
      if (val != null)
        return "Please refer to Delivery and returns info section";
    }

    shipment = doc.getElementById("shippingRestrictionsLink");
    if (shipment != null) {
      return shipment.text();
    } else {
      return "See delivery and returns info";
    }
  }

  @Override
  public String getBrand() {
    Element brand = doc.selectFirst("span[itemprop='brand'] span[itemprop='name']");
    if (brand != null) {
      return brand.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-description li"));
  }
}
