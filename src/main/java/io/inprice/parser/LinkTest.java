package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.au.AppliancesOnline;
import io.inprice.parser.websites.au.Kogan;
import io.inprice.parser.websites.ca.BestBuy;
import io.inprice.parser.websites.ca.CanadianTire;
import io.inprice.parser.websites.it.Euronics;

public class LinkTest {
	
	/**
	 * Australia
	 * appliancesonline, bigw, harveynorman, kogan, thegoodguys
	 * 
	 */
	
	/**
	 * Canada
	 * bestbuy, canadiantire
	 * 
	 */
	
	/**
	 * Germany
	 * euronics
	 * 
	 */

	private static String[] urls = {
		"https://www.appliancesonline.com.au/andoo/product/universe-bed-co-stardream-double-mattress-nsdpsmib001"
	};

	public static void main(String[] args) throws InterruptedException {
		Platform platform = new Platform();
		platform.setName("Test Site");
		platform.setDomain("Solo Test");

		for (String url: urls) {
			/*
			new Thread(new Runnable() {

				@Override
				public void run() {
				*/
		  		Link link = new Link(url);
		  		link.setId(1l);
		  		link.setRetry(1);
		  		link.setPlatform(platform);
		  		link.setPlatformId(platform.getId());

		  		Website website = new AppliancesOnline();
		  		website.check(link);
		  		printout(link);
				}
				/*
			}).start();
		}
				 */
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
