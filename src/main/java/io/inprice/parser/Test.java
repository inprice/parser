package io.inprice.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class Test {

	public static void main2(String[] args) {
		String url = "https://www.asos.com/collusion/collusion-argyl-tank-cardigan-co-ord-multi/grp/68617?colourWayId=200613121&cid=120291";
		String newUrl = url.replaceAll("cid\\=\\d+", "cid=" +  RandomStringUtils.randomNumeric(5));

		System.out.println(url);
		System.out.println(newUrl);
	}
	
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
			html = new String(Files.readAllBytes(Paths.get("/home/mdpinar/zalando.html")));

			List<String> attributes = findAttributes(html);
	    for (String attrs: attributes) {
	    	System.out.println(attrs);
	    }
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static List<String> findAttributes(String html) {
  	List<String> attrList = new ArrayList<>();

  	String starting = "\"attributes\":";
  	String ending = "\"}]";

  	int prePosition = html.indexOf(starting);
  	while (prePosition != -1) {
	  	String attrs = findAPart(html, starting, ending, prePosition, true);
	  	attrList.add(attrs);
	  	prePosition = html.indexOf(starting, prePosition+1);
  	}

  	return attrList;
	}
	
	private static String findAPart(String html, String starting, String ending, int startPointOffset, boolean isEndingIncluded) {
		int start = html.indexOf(starting, startPointOffset) + starting.length();
		int end = html.indexOf(ending, start) + (isEndingIncluded ? ending.length() : 0);

		if (start > -1 && start < end) {
			return html.substring(start, end);
		}
		return null;
	}

}
