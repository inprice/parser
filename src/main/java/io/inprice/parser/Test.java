package io.inprice.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.inprice.parser.helpers.Global;
import io.inprice.parser.helpers.StringHelpers;

public class Test {

	public static void main(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/TestSite-1.html")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(StringHelpers.escapeJSON(html.replaceAll("\"description\".*(\"offers\":)", "$1")));

	}
/*
	private static String findAPart(String html, String starting, String ending, int plus, int startPointOffset) {
		int start = html.indexOf(starting) + (startPointOffset <= 0 ? starting.length() : 0);
		int end = html.indexOf(ending, start) + plus;

		if (start > starting.length() && end > start) {
			return html.substring(start + startPointOffset, end);
		}

		return null;
	}
*/
}
