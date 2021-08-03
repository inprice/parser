package io.inprice.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.inprice.parser.helpers.Global;

public class Test {
/*
	public static void main(String[] args) {
		Global.initWebDriver();
		for (int i = 0; i < 5; i++) {
			Global.getWebDriver().get("https://urun.n11.com/okul-cantalari/polo-single-lacivert-kirmizi-baski-1900-denye-sirt-cantasi-30025-P500278585");
			System.out.println(i);
		}
		Global.closeWebDriver();
	}
*/
	public static void main1(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/TestSite-1.html")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String ind = "dataLayer.push(";
		String rawJson = findAPart(html, ind, "});", 1, ind.length());

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

}
