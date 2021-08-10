package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.uk.EuronicsUK;

public class LinkTest {
	
	//singly   --> walmart.ca
	//blocked  --> eprice.it

	// Australia --> appliancesonline, bigw, harveynorman, kogan, thegoodguys
	// Canada    --> bestbuy, canadiantire, walmart
	// Germany   --> euronics, mediamarkt, notebooksbillinger, otto
	// Spain     --> electroking, euronics, 101gigas, ulabox
	// France    --> auchan, cdiscount, fnac, laredoute
	// Italy     --> euronics, mediaworld
	// Netherland--> bol, coolblue, debijenkorf, wehkamp
	// Turkey    --> gittigidiyor, hepsiburada, n11, teknosa, trendyol
	// U.Kingdom --> argos, asdadirect, asdagrocery, asos, currys, debenhams, euronics,

	private static String[] urls = {
		"https://www.euronics.co.uk/catalogue/small-appliances/small-cooking-appliances/multi-cookers/ninja-ag551uk-foodi-max-health-grill-and-air-fryer-blackstainless-steel/p/307AG551UK",
		"https://www.euronics.co.uk/catalogue/tv-and-entertainment/43-to-54-inch-televisions/43-to-54-inch-oled-televisions/43-to-54-inch-oled-4k-uhd-televisions/lg-oled48cx5lc-48-4k-oled-smart-tv/p/101OLED48CX5LC",
		"https://www.euronics.co.uk/catalogue/refrigeration/american-style-fridge-freezers/samsung-rs67a8811s9-american-style-fridge-freezer-matt-stainless/p/245RS67A8811S9",
		"https://www.euronics.co.uk/catalogue/small-appliances/small-cooking-appliances/multi-cookers/ninja-op100uk-foodi-mini-6-in-1-multi-cooker-black/p/307OP100UK",
		"https://www.euronics.co.uk/catalogue/laundry/washing-machines/integrated-washing-machines/bosch-wiw28301gb-integrated-8kg-1400-spin-washing-machine-with-varioperfect-white/p/296WIW28301GB"
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

		  		Website website = new EuronicsUK();
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
