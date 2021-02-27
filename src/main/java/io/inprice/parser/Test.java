package io.inprice.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Test {
  
  public static void main(String[] args) throws IOException {
  	String doc = new String(Files.readAllBytes( Paths.get("/home/mdpinar/tmp/apple-test.html")));

  	String rawJson = findAPart(doc, "![CDATA[{\"layout\"", "}]]", 1, 8);
  	
  	JSONObject info = null;
  	JSONObject price = null;
  	JSONArray details = null;
  	
		if (StringUtils.isNotBlank(rawJson)) {
			JSONObject json = new JSONObject(rawJson);
			if (json != null && json.has("model")) {
				JSONObject model = json.getJSONObject("model");
				if (model != null) {
					if (model.has("articleInfo")) {
						info = model.getJSONObject("articleInfo");
					}
					if (model.has("displayPrice")) {
						price = model.getJSONObject("displayPrice");
					}
					if (model.has("productDetailsCluster")) {
						details = model.getJSONArray("productDetailsCluster");
					}
				}
			}
		}
  	System.out.println("");
  }

  private static String findAPart(String html, String starting, String ending, int plus, int startPointOffset) {
		int start = html.indexOf(starting) + (startPointOffset <= 0 ? starting.length() : 0);
		int end = html.indexOf(ending, start) + plus;

		if (start > starting.length() && end > start) {
			return html.substring(start+startPointOffset, end);
		}

		return null;
	}

}