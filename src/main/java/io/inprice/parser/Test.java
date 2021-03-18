package io.inprice.parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import io.inprice.common.models.LinkSpec;

public class Test {

	public static void main(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/saturn-de.html")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject json = null;
		
		String preload = findAPart(html, "__PRELOADED_STATE__ = ", ";</script>", 0, 0);
		if (preload != null) {
			preload = preload.replaceAll("undefined", "\"\"");
			json = new JSONObject(preload);
			
			//finding SKU
			String url = getUrl();
			int lastDashIx = url.lastIndexOf("-");
			String sku = url.substring(lastDashIx+1, url.lastIndexOf("."));
			
			JSONObject apolloState = null;
			if (json.has("apolloState")) {
				apolloState = json.getJSONObject("apolloState");
				
				JSONObject root = apolloState.getJSONObject("ROOT_QUERY");
				
				JSONObject priceJson = root.getJSONObject("price({\"id\":\""+sku+"\"})");
				JSONObject availJson = root.getJSONObject("availability({\"id\":\""+sku+"\"})");
				JSONObject shipJson = root.getJSONObject("shippingCost({\"id\":\""+sku+"\"})");
				JSONObject detailsJson = apolloState.getJSONObject("GraphqlProduct:"+sku);

				BigDecimal price = priceJson.getBigDecimal("price");
				
				boolean isAvailable = false;
				if (availJson != null && availJson.has("delivery")) {
					JSONObject delivery = availJson.getJSONObject("delivery");
					if (delivery != null && delivery.has("quantity")) {
						isAvailable = delivery.getInt("quantity") > 0;
					}
				}

				String shipping = null;
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
				
				String name = null;
				String brand = null;
				List<LinkSpec> specList = null;
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
				
				System.out.println("SKU: " + sku);
				System.out.println("NAME: " + name);
				System.out.println("AVAILABLE: " + isAvailable);
				System.out.println("PRICE: " + price);
				System.out.println("BRAND: " + brand);
				System.out.println("SHIPPING: " + shipping);
				
				if (specList != null) {
					System.out.println("-------------");
					specList.forEach(s -> System.out.println(s.getKey() + ": " + s.getValue()));
				}
			}
		}
		
	}
	
	private static String getUrl() {
		return "https://www.saturn.de/de/product/_garmin-running-dynamics-pod-2253522.html";
	}

	private static String findAPart(String html, String starting, String ending, int plus, int startPointOffset) {
		int start = html.indexOf(starting) + (startPointOffset <= 0 ? starting.length() : 0);
		int end = html.indexOf(ending, start) + plus;

		if (start > starting.length() && end > start) {
			return html.substring(start + startPointOffset, end);
		}

		return null;
	}

}
