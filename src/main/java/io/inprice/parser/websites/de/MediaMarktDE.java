package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for MediaMarkt and Saturn Deutschland (protected by cloudflare!!!)
 *
 * All the data is placed in PRELOAD_STATE variable
 *
 * @author mdpinar
 */
public class MediaMarktDE extends AbstractWebsite {

	private boolean isAvailable;
	private String sku = Consts.Words.NOT_AVAILABLE;
	private String shipping = Consts.Words.NOT_AVAILABLE;
	private String name = Consts.Words.NOT_AVAILABLE;
	private BigDecimal price = BigDecimal.ZERO;
	private String brand = Consts.Words.NOT_AVAILABLE;
	private List<LinkSpec> specList;
  
  @Override
  protected Renderer getRenderer() {
  	return Renderer.CHROME;
  }
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		System.out.println(html);

		String preload = findAPart(html, "__PRELOADED_STATE__ = ", ";</script>", 0, 0);
		if (preload != null) {
			JSONObject json = new JSONObject(preload.replaceAll("undefined", "\"\""));

			//finding SKU
			String url = getUrl();
			sku = url.substring(url.lastIndexOf("-")+1, url.length()-5);
			
			JSONObject apolloState = null;
			if (json.has("apolloState")) {
				apolloState = json.getJSONObject("apolloState");
				
				JSONObject root = apolloState.getJSONObject("ROOT_QUERY");

				JSONObject priceJson = null;
				JSONObject availJson = null;
				JSONObject shipJson = null;
				JSONObject detailsJson = null;
				
				if (root.has("price({\"id\":\""+sku+"\"})")) priceJson = root.getJSONObject("price({\"id\":\""+sku+"\"})");
				if (root.has("availability({\"id\":\""+sku+"\"})")) availJson = root.getJSONObject("availability({\"id\":\""+sku+"\"})");
				if (root.has("shippingCost({\"id\":\""+sku+"\"})")) shipJson = root.getJSONObject("shippingCost({\"id\":\""+sku+"\"})");
				if (apolloState.has("GraphqlProduct:"+sku)) detailsJson = apolloState.getJSONObject("GraphqlProduct:"+sku);

				if (priceJson != null && priceJson.has("price")) price = priceJson.getBigDecimal("price");
				
				isAvailable = false;
				if (availJson != null && availJson.has("delivery")) {
					JSONObject delivery = availJson.getJSONObject("delivery");
					if (delivery != null && delivery.has("quantity")) {
						isAvailable = delivery.getInt("quantity") > 0;
					}
				}

				if (shipJson != null) {
					Object isFree = shipJson.get("freeShipping");
					if (isFree != null && !isFree.toString().equals("null") && StringUtils.isNotBlank(isFree.toString())) {
						shipping = isFree.toString();
					} else {
						shipping = shipJson.getString("label");
						BigDecimal cost = shipJson.getBigDecimal("cost");
						if (cost != null) shipping += ": " + cost.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}
				}
				
				if (detailsJson != null) {
					if (detailsJson.has("title")) name = detailsJson.getString("title");
					if (detailsJson.has("manufacturer")) brand = detailsJson.getString("manufacturer");
					
					if (detailsJson.has("mainFeatures")) {
						JSONArray features = detailsJson.getJSONArray("mainFeatures");
						if (features != null && features.length() > 0) {
							specList = new ArrayList<>(features.length());
							for (int i = 0; i < features.length(); i++) {
								JSONObject feature = features.getJSONObject(i);
								specList.add(new LinkSpec(feature.getString("name"), feature.getString("value")));
							}
						}
					}
				}
			}
		}
	}

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public BigDecimal getPrice() {
    return price;
  }

  @Override
  public String getBrand() {
    return brand;
  }

  @Override
  public String getShipment() {
    return shipping;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    return specList;
  }

}
