package io.inprice.parser.websites.au;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.StringUtils;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for BigW Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class BigW extends AbstractWebsite {

  @Override
  protected JSONObject getJsonData() {
    final String prodData = findAPart(doc.html(), "'products': [", "}]", 1);
    if (prodData != null) {
    	return new JSONObject(StringUtils.fixQuotes(prodData));
    }

    return super.getJsonData();
  }

  @Override
  public boolean isAvailable() {
    return (doc.getElementById("increase_quantity_JS") != null);
  }

  @Override
  public String getSku() {
    if (json != null && json.has("id")) {
      return json.getString("id");
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
    if (json != null && json.has("price")) {
      return json.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getSeller() {
    return "Big W";
  }

  @Override
  public String getShipment() {
    return "In-store pickup";
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
    	return json.getString("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
  	List<LinkSpec> specList = getKeyValueSpecList(doc.select("div.tab-Specification li"), "div.meta", "div.subMeta");
  	List<LinkSpec> featureList = getValueOnlySpecList(doc.select("div.contentList li"), "div.meta");

  	if (specList != null && featureList == null) return specList;
  	if (featureList != null && specList == null) return featureList;

  	featureList.addAll(specList);
  	return featureList;
  }

}
