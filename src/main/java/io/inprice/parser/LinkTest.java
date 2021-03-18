package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.de.MediaMarktDE;

public class LinkTest {

	private static Website website = new MediaMarktDE();
	private static String url = "https://www.saturn.de/de/product/_hp-officejet-6950-2166497.html";
	
	public static void main(String[] args) {
		//HTMLUNIT_POOL.setup();

		Platform platform = new Platform();
		platform.setName("Test Site");
		platform.setDomain("Solo Test");

		Link link = new Link(url);
		link.setId(1l);
		link.setRetry(1);
		link.setPlatform(platform);
		
		website.check(link);
		printout(link);
		
		//HTMLUNIT_POOL.shutdown();
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
