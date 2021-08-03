package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.au.Kogan;
import io.inprice.parser.websites.au.TheGoodGuys;

public class LinkTest {
	
	/**
	 * Turkey
	 * trendyol, teknosa, amazon, MediaMarkt
	 * N11, Gittitgidiyor, hepsiburada
	 */
	
	/**
	 * Australia
	 * appliancesonline, bigw, harveynorman, kogan, thegoodguys
	 * 
	 */

	private static String[] urls = {
		"https://www.thegoodguys.com.au/delonghi-60cm-stainless-steel-dishwasher-dedw6112s",
		"https://www.thegoodguys.com.au/westinghouse-609l-french-door-refrigerator-wqe6870ba",
		"https://www.thegoodguys.com.au/hp-pavilion-x360-2-in-1-laptop-2q4e6pa",
		"https://www.thegoodguys.com.au/jabra-talk-5-bluetooth-ear-piece-4305788",
		"https://www.thegoodguys.com.au/lg-65-inches-a1-4k-uhd-self-lit-oled-smart-tv-oled65a1pta"
	};

	public static void main(String[] args) throws InterruptedException {
		Platform platform = new Platform();
		platform.setName("Test Site");
		platform.setDomain("Solo Test");

		for (String url: urls) {
			new Thread(new Runnable() {

				@Override
				public void run() {
		  		Link link = new Link(url);
		  		link.setId(1l);
		  		link.setRetry(1);
		  		link.setPlatform(platform);
		  		link.setPlatformId(platform.getId());

		  		Website website = new TheGoodGuys();
		  		website.check(link);
		  		printout(link);
				}
				
			}).start();
		}
	}
	
	private static void printout(Link link) {
		System.out.println("Sku: " + link.getSku());
		System.out.println("Name: " + link.getName());
		System.out.println("Price: " + link.getPrice());
		System.out.println("Brand: " + link.getBrand());
		System.out.println("Seller: " + link.getSeller());
		System.out.println("Shipment: " + link.getShipment());

		System.out.println("--------------------------");
		System.out.println("Status: " + link.getStatus());
		System.out.println("Http Status: " + link.getHttpStatus());
		System.out.println("Problem: " + link.getProblem());
		System.out.println("--------------------------");

		if (link.getSpecList() != null && link.getSpecList().size() > 0) {
			System.out.println("Spec List");
			link.getSpecList().forEach(s -> System.out.println(" - Key: " + s.getKey() + ", Value: " + s.getValue()));
		}
	}
	
}
