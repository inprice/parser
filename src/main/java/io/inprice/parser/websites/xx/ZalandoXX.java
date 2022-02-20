package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.inprice.common.helpers.GlobalConsts;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.StringHelper;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Zalando, Global
 *
 * URL depends on the country
 * DE --> https://www.zalando.de
 * ES --> https://www.zalando.es
 * FR --> https://www.zalando.fr
 * IT --> https://www.zalando.it
 * NL --> https://www.zalando.nl
 * UK --> https://www.zalando.co.uk
 *
 * @author mdpinar
 */
public class ZalandoXX extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;
  private boolean isAvailable;

	@Override
	public ParseStatus startParsing(Link link, String html) {
		dom = Jsoup.parse(html);
		
    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (CollectionUtils.isNotEmpty(dataEL)) {
    	for (DataNode dNode : dataEL.dataNodes()) {
    		String edited = dNode.getWholeData().replaceAll("&quot;", "\"");
        JSONObject data = new JSONObject(StringHelper.escapeJSON(edited));
        if (data.has("@type")) {
          String type = data.getString("@type");
          if (type.equals("Product")) {
          	json = data;
            if (json.has("offers")) {
          		JSONArray offersArr = json.getJSONArray("offers");
          		for (int i = 0; i < offersArr.length(); i++) {
          			offers = offersArr.getJSONObject(i);
                String availability = offers.getString("availability").toLowerCase();
                if (availability.contains("instock") || availability.contains("preorder")) {
                	isAvailable = true;
                	break;
                }
            	}
          		return OK_Status();
            }
          }
        }
      }
    }
		return ParseStatus.PS_NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    if (json != null && json.has("name")) {
    	return json.getString("name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    if (offers != null && offers.has("price")) {
      return new BigDecimal(offers.getString("price"));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
    	JSONObject brand = json.getJSONObject("brand");
    	if (brand.has("name")) return brand.getString("name");
    }
    return GlobalConsts.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
  	return Consts.Words.CHECK_DELIVERY_CONDITIONS;
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("div[data-testid^='pdp-accordion'] .b3yJDY"), "span:nth-child(1)", "span:nth-child(2)");
  	/*
  	List<String> attrs = findParts(dom.html(), "\"attributes\":", "\"}]");

  	System.out.println("---");
    for (String attr: attrs) {
    	System.out.println(attr);
    }
  	System.out.println("---");
  	
  	Set<LinkSpec> specs = new HashSet<>();
  	attrs.forEach(attr -> {
  		JSONObject attj = new JSONObject(attr);
  		specs.add(new LinkSpec(attj.getString("key"), attj.getString("value")));
  	});
  	
  	return specs;
	 */
  }
  
}
