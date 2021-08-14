package io.inprice.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Test {

	public static void main1(String[] args) {
		String url = "https://www.amazon.com/VIZIO-Chromecast-Processor-V-Gaming-V705-J03/dp/B092Q6QNC1/ref=sr_1_11?dchild=1&field-shipping_option-bin=3242350011&pf_rd_i=16225009011&pf_rd_m=ATVPDKIKX0DER&pf_rd_p=85a9188d-dbd5-424e-9512-339a1227d37c&pf_rd_r=QG1CNPARR3KYDCQP9D9F&pf_rd_s=merchandised-search-5&pf_rd_t=101&qid=1628778992&rnid=1266092011&s=electronics&sr=1-11";
		int pos = url.indexOf("/dp/")+4;
		String asin = url.substring(pos, pos+10);

		StringBuilder prodUrl = new StringBuilder("https://www.amazon.com/gp/aod/ajax/ref=dp_aod_unknown_mbc?asin=");
    prodUrl.append(asin);
		System.out.println(prodUrl);
	}
	
	public static void main(String[] args) {
		String html = "";
		try {
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/tmp/TestSite-1.html")));
			Document dom = Jsoup.parse(html);
			
	    Element val = dom.selectFirst(".row.mb-1");
	    System.out.println(val.text());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

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
