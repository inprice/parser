package io.inprice.parser;

import org.apache.commons.collections4.CollectionUtils;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.xx.AmazonXX;

public class LinkTest {
	
	//singly   --> walmart.ca
	//blocked  --> eprice.it, mediamarkt.es

	// Australia --> appliancesonline, bigw, harveynorman, kogan, thegoodguys
	// Canada    --> bestbuy, canadiantire, walmart
	// Germany   --> euronics, lidl, mediamarkt, notebooksbillinger, otto
	// Spain     --> electroking, euronics, 101gigas, ulabox
	// France    --> auchan, cdiscount, fnac, laredoute
	// Italy     --> euronics, mediaworld
	// Netherland--> bol, coolblue, debijenkorf, wehkamp
	// Turkey    --> gittigidiyor, hepsiburada, n11, teknosa, trendyol
	// U.Kingdom --> argos, asdadirect, asdagrocery, asos, currys, debenhams, euronics, newlook, zavvi
	// U.States  --> bestbuy, bonanza, etsy, walmart, target
	// Global    --> amazon, apple, bonprix, ebay, lidl, mediamarkt, vidaxl, zalando

	private static String[] urls = {
		"https://www.amazon.com.tr/Ta%C3%A7-Baby-D%C3%BCd%C3%BCkl%C3%BC-Tencere-Kirmizi/dp/B084W1N3K2?ref_=Oct_s9_apbd_orecs_hd_bw_bEkQn0h&pf_rd_r=PN0DW37HSCM8QNZP5T03&pf_rd_p=83abbddc-e87f-5da7-8627-0bc095b56370&pf_rd_s=merchandised-search-10&pf_rd_t=BROWSE&pf_rd_i=13511956031"
	};

	public static void main(String[] args) throws InterruptedException {
		Platform platform = new Platform();
		platform.setName("Test Site");
		platform.setDomain("Solo Test");
		platform.setCountry("Global");

		for (String url: urls) {
			new Thread(new Runnable() {

				@Override
				public void run() {
		  		Link link = new Link(url);
		  		link.setId(1l);
		  		link.setRetry(1);
		  		link.setPlatform(platform);
		  		link.setPlatformId(platform.getId());

		  		Website website = new AmazonXX();
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
		System.out.println("Parse Code: " + link.getParseCode());
		System.out.println("Parse Problem: " + link.getParseProblem());
		System.out.println("--------------------------");

		if (CollectionUtils.isNotEmpty(link.getSpecList())) {
			System.out.println("Spec List");
			link.getSpecList().forEach(s -> System.out.println(" - Key: " + s.getKey() + ", Value: " + s.getValue()));
		}
	}
	
}
