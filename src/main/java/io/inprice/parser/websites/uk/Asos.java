package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;
import kong.unirest.HttpResponse;

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
  private String sku;
  private String name;
  private String brandName;
  private String shipment;
  private JSONObject offer;

  @Override
  protected void getJsonData() {
    final String prodData = findAPart(doc.html(), "window.asos.pdp.config.product =", "};", 1);
    if (StringUtils.isNotBlank(prodData)) {
      offer = new JSONObject(prodData);
    }

    if (offer != null) {
      sku = "" + offer.getInt("id");
      isAvailable = offer.getBoolean("isInStock");
      name = offer.getString("name");
      brandName = offer.getString("brandName");
      JSONObject shipping = offer.getJSONObject("shippingRestrictions");
      if (shipping != null && shipping.has("shippingRestrictionsLabel")) {
        shipment = shipping.getString("shippingRestrictionsLabel");
      }
    } else {
      sku = getSku();
    }

    HttpResponse<String> 
      response = 
        httpClient
        .get("https://www.asos.com/api/product/catalogue/v3/stockprice?store=ROW&productIds=" + sku);
    if (response != null && response.getStatus() > 0 && response.getStatus() < 400) {
    	
    	boolean hasDataProblem = true;

      if (response.getBody() != null && StringUtils.isNotBlank(response.getBody())) {
        JSONArray items = new JSONArray(response.getBody());
        if (items.length() > 0) {
          JSONObject parent = items.getJSONObject(0);

          if (parent.has("productPrice")) {
            JSONObject pprice = parent.getJSONObject("productPrice");
            price = pprice.getJSONObject("current").getBigDecimal("value");
          }

          if (!isAvailable && parent.has("variants")) {
            JSONArray variants = parent.getJSONArray("variants");
            if (variants.length() > 0) {
            	hasDataProblem = false;
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
      }
      
      if (hasDataProblem) {
      	setLinkStatus(LinkStatus.INVALID_DATA, "Invalid data structure!");
      }
    } else {
      setLinkStatus(response);
    }
  }

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    if (StringUtils.isNotBlank(sku)) return sku;

    Element val = doc.selectFirst("span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    String[] urlChunks = getUrl().split("/");
    if (urlChunks.length > 1) {
      String prdId = urlChunks[urlChunks.length - 1];
      return prdId.substring(0, prdId.indexOf("?"));
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (StringUtils.isNotBlank(name)) return name;

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
    Element val = doc.selectFirst("span[itemprop='seller'] span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    if (StringUtils.isNotBlank(shipment)) return shipment;

    Element val = doc.selectFirst("#shipping-restrictions .shipping-restrictions");
    if (val != null && StringUtils.isNotBlank(val.attr("style"))) {
      String style = val.attr("style");
      if (style != null)
        return "Please refer to Delivery and returns info section";
    }

    val = doc.getElementById("shippingRestrictionsLink");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    } else {
      return "See delivery and returns info";
    }
  }

  @Override
  public String getBrand() {
    if (StringUtils.isNotBlank(brandName)) return brandName;

    Element val = doc.selectFirst("span[itemprop='brand'] span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-description li"));
  }

}
