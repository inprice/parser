package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.tr.Trendyol;

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

	private static String[] urls = {
		"https://www.trendyol.com/aqua-di-polo-1987/kadin-kol-saati-apsv1-a9371-km222-p-32043783?boutiqueId=575927&merchantId=2471",
		"https://www.trendyol.com/spectrum/unisex-siyah-miknatisli-dokunmatik-kol-saati-xt250131-p-50728396?boutiqueId=575537&merchantId=345375",
		"https://www.trendyol.com/istliv/kadin-erkek-su-gecirmez-dokunmatik-kol-saati-p-77791901?boutiqueId=575717&merchantId=195779",
		"https://www.trendyol.com/aqua-di-polo-1987/unisex-kol-saati-apl12c350d01-p-4022508?boutiqueId=575927&merchantId=2471",
		"https://www.trendyol.com/duke-nickle/erkek-kol-saati-veh28019a-p-32183801?boutiqueId=575815&merchantId=106615"
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

		  		Website website = new Trendyol();
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
