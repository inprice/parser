package io.inprice.parser;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;

public class Test {

	public static void main(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/TestSite-1.html")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String rawJson = null;
		if (html.indexOf("}]},") > -1) {
			rawJson = findAPart(html, "\\\"product\\\":{\\\"product\\\":", "}]},", 3, 0);
		} else {
			rawJson = findAPart(html, "\\\"product\\\":{\\\"product\\\":", "}}\",", 2, 0);
		}
		rawJson = rawJson.replace("\\\"", "\"");
		rawJson = rawJson.replace(":\"{\"", ":{\"");
		rawJson = rawJson.replace("]}}\",", "]}},");
		rawJson = rawJson.replace("\\u003c", "<");
		rawJson = rawJson.replace("\\u003e", ">");
		rawJson = rawJson.replace("\\\"", "\"");
  	
  	System.out.println(rawJson);
		
	}

	private static String findAPart(String html, String starting, String ending, int plus, int startPointOffset) {
		int start = html.indexOf(starting) + (startPointOffset <= 0 ? starting.length() : 0);
		int end = html.indexOf(ending, start) + plus;

		if (start > starting.length() && end > start) {
			return html.substring(start + startPointOffset, end);
		}

		return null;
	}

	private static List<LinkSpec> getKeyValueSpecList(Elements specs, String keySelector, String valueSelector) {
		List<LinkSpec> specList = null;
		if (specs != null && specs.size() > 0) {
			specList = new ArrayList<>();
			for (Element spec : specs) {
				Element key = spec.selectFirst(keySelector);
				Element value = spec.selectFirst(valueSelector);
				if (key != null || value != null) {
					specList.add(
					    new LinkSpec((key != null ? key.text().replaceAll(":", "") : ""), (value != null ? value.text() : "")));
				}
			}
		}
		return specList;
	}

}
