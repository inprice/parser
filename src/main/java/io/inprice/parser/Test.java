package io.inprice.parser;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class Test {
	
	public static void main(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/cdiscount.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject root = new JSONObject(html.replaceAll("\\p{C}", ""));
		System.out.println(root);
	}

	public static void main1(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/TestSite-1.html")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		String rawJson = findAPart(html, "\\\"product\\\":{\\\"product\\\":", "}}\",", 2, 0);
		rawJson = rawJson.replace("\\\"", "\"");
		rawJson = rawJson.replace(":\"{\"", ":{\"");
		rawJson = rawJson.replace("]}}\",", "]}},");
		
		JSONObject json = new JSONObject(rawJson);
		
		if (json != null && json.has("id")) {
			System.out.println("SKU: " + json.getLong("id"));
		}
		
		if (json != null && json.has("name")) {
			System.out.println("NAME: " + json.getString("name"));
		}
		
		if (json != null && json.has("price")) {
			JSONObject price = json.getJSONObject("price");
			System.out.println("PRICE: " + price.getBigDecimal("value").setScale(2, RoundingMode.HALF_UP));
		}
		
		if (json != null && json.has("brand")) {
			JSONObject brand = json.getJSONObject("brand");
			System.out.println("BRAND: " + brand.getString("name"));
		}
		
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
