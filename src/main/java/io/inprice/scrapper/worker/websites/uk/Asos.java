package io.inprice.scrapper.worker.websites.uk;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
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

  public Asos(Competitor competitor) {
    super(competitor);
  }

  @Override
  protected JSONObject getJsonData() {
    CompetitorStatus preStatus = getCompetitorStatus();
    setCompetitorStatus(CompetitorStatus.NO_DATA);

    final String sku = getSku();

    HttpResponse<String> response = httpClient
        .get("https://www.asos.com/api/product/catalogue/v3/stockprice?currency=EUR&store=ROW&productIds=" + sku);
    if (response != null && response.getStatus() > 0 && response.getStatus() < 400) {

      if (response.getBody() != null && StringUtils.isNotBlank(response.getBody())) {
        JSONArray items = new JSONArray(response.getBody());
        if (items.length() > 0) {
          JSONObject parent = items.getJSONObject(0);

          if (parent.has("productPrice")) {
            JSONObject pprice = parent.getJSONObject("productPrice");
            price = pprice.getJSONObject("current").getBigDecimal("value");
            setCompetitorStatus(preStatus);
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
        setCompetitorStatus(CompetitorStatus.NO_DATA);
      }
    } else {
      setCompetitorStatus(response);
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("span[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
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
    Element val = doc.selectFirst("span[itemprop='seller'] span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = doc.selectFirst("#shipping-restrictions .shipping-restrictions");
    if (val != null && StringUtils.isNotBlank(val.attr("style"))) {
      String style = val.attr("style");
      if (style != null)
        return "Please refer to Delivery and returns info section";
    }

    val = doc.getElementById("shippingRestrictionsCompetitor");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    } else {
      return "See delivery and returns info";
    }
  }

  @Override
  public String getBrand() {
    Element val = doc.selectFirst("span[itemprop='brand'] span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<CompetitorSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("div.product-description li"));
  }
}
