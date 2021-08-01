package io.inprice.parser.websites.tr;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for n11 Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class N11 extends AbstractWebsite {

	private Document dom;
  private JSONObject prod;

  /**
   * Protected by akamai!
   */
  @Override
	protected Renderer getRenderer() {
		return Renderer.HEADLESS;
	}

	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);

		String ind = "dataLayer.push(";
		String rawJson = findAPart(html, ind, "});", 1, ind.length());

    if (StringUtils.isNotBlank(rawJson)) {
      prod = new JSONObject(rawJson);
    }
	}

  @Override
  public boolean isAvailable() {
    if (prod != null && prod.has("pIsInStock")) return prod.getString("pIsInStock").equals("1");
    return false;
  }

  @Override
  public String getSku() {
    if (prod != null && prod.has("pId")) return prod.getString("pId");
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (prod != null && prod.has("title")) return prod.getString("title");
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	String val = null;

    if (prod != null) {
    	if (prod.has("pDiscountedPrice"))
    		val = prod.getString("pDiscountedPrice");
    	else if (prod.has("pOriginalPrice"))
    		val = prod.getString("pOriginalPrice");
    }

    if (StringUtils.isNotBlank(val)) {
      return new BigDecimal(cleanDigits(val));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
  	String val = null;

  	if (prod != null) {
    	if (prod.has("pBrand"))
    		val = prod.getString("pBrand");
    	else
    		val = prod.getString("sellerNickname");
  	}

    if (StringUtils.isNotBlank(val)) {
    	return val;
    }
  	
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    if (prod != null && prod.has("sellerNickname")) return prod.getString("sellerNickname");
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst(".shipment-detail-container .cargoType");
    if (val == null || StringUtils.isBlank(val.text())) {
      val = dom.selectFirst(".delivery-info_shipment span");
      if (val == null || StringUtils.isBlank(val.text())) {
      	val = dom.selectFirst(".delInfo b");
      }
    }

    if (val != null) {
      return val.text().replaceAll(":", "");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    Elements specs = dom.select("div.feaItem");
    String keySelector = ".label";
    String valSelector = ".data";

    if (specs == null || specs.isEmpty()) {
      specs = dom.select("li.unf-prop-list-item");
      keySelector = "p.unf-prop-list-title";
      valSelector = "p.unf-prop-list-prop";
    }

    return getKeyValueSpecs(specs, keySelector, valSelector);
  }

}
