package io.inprice.parser.websites.it;

import kong.unirest.HttpResponse;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for vidaXL Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class VidaXL extends AbstractWebsite {

  private JSONObject current;

  public VidaXL(Link link) {
    super(link);
  }

  public String getAuctionId() {
    Element val = doc.getElementById("auctionId");
    if (val != null && StringUtils.isNotBlank(val.val())) {
      return val.val();
    }
    return null;
  }

  @Override
  protected JSONObject getJsonData() {
    String auctionId = getAuctionId();
    if (auctionId != null) {
      HttpResponse<String> response = httpClient
          .get("https://www.vidaxl.it/platform/index.php?m=auction&a=getAuctionsList&id=" + auctionId);
      if (response.getStatus() == 200 && StringUtils.isNotBlank(response.getBody())) {
        JSONObject auction = new JSONObject(response.getBody());
        if (auction.has("current")) {
          current = auction.getJSONObject("current");
        }
      }
    }

    return null;
  }

  @Override
  public boolean isAvailable() {
    Element inStock = doc.selectFirst("div#not-available div.false");
    return (inStock != null);
  }

  @Override
  public String getSku() {
    Element val = doc.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    val = doc.selectFirst("input[name='hidden_sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("value"))) {
      return val.attr("value");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = doc.selectFirst("h1[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = doc.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (current != null && current.has("price")) {
      return new BigDecimal(cleanDigits(current.getString("price")));
    }

    Element val = doc.selectFirst("meta[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    Element val = doc.selectFirst("meta[itemprop='seller']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return "VidaXL";
  }

  @Override
  public String getShipment() {
    StringBuilder sb = new StringBuilder();

    Element val = doc.selectFirst("div.delivery-name");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = doc.selectFirst("div.delivery-info");
    }

    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text());
      sb.append(". ");
    }

    val = doc.selectFirst("div.shipping-from");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text());
      sb.append(". ");
    }

    val = doc.selectFirst("div.delivery-seller");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      sb.append(val.text());
    }

    if (sb.length() == 0) sb.append("NA");

    return sb.toString().replaceAll(" Disponibile Non disponibile", "");
  }

  @Override
  public String getBrand() {
    final String brand = findAPart(doc.html(), "\"brand\":\"", "\"");

    if (brand != null) {
      return brand;
    }

    return "VidaXL";
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return getValueOnlySpecList(doc.select("ul.specs li"));
  }
}
