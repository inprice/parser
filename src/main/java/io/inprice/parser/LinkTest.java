package io.inprice.parser;

import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.AbstractWebsite;
import io.inprice.parser.websites.xx.WalmartXX;

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

	// U.States  --> AmazonXX, EbayXX, AppleXX, BestBuyUS, BonanzaUS, EtsyUS, LidlUS, TargetUS, VidaXLXX, WalmartXX

	// Canada    --> WalmartXX

	// Germany   --> AmazonXX
	
	private static String[] urls = {
			//"https://urun.n11.com/okul-cantalari/polo-single-lacivert-kirmizi-baski-1900-denye-sirt-cantasi-30025-P500278585"
			//"https://www.walmart.com/ip/Xiaomi-MI-Band-6-Smart-Watch-Fitness-Tracker-SpO2-Monitor-30-Sports-Modes-1-56-In-AMOLED-Screen-5ATM-Waterproof-Wristband-Bracelet-Black/575900423?athbdg=L1700"
			//"https://www.amazon.de/-/en/3059797-Scholl-GelActiv-Work-Insoles/dp/B07FCCDN45/ref=sr_1_7?c=ts&keywords=schuhpflegemittel+%26+zubeh%C3%B6r&qid=1645062153&rdc=1&s=shoes&sr=1-7&ts_id=1760548031"
			//"https://www.ebay.de/itm/144165957595?_trkparms=%26rpp_cid%3D5ca1fa15f041412ce32f28d7%26rpp_icid%3D5ca1fa15f041412ce32f28d6&_trkparms=pageci%3A0c3f0d4e-8fd4-11ec-9fbd-aae3160ec182%7Cparentrq%3A07037fc017f0a45c7857f3f5fff1fbf0%7Ciid%3A1"
			//"https://www.apple.com/shop/buy-mac/macbook-pro/14-inch-space-gray-10-core-cpu-16-core-gpu-1tb"
			//"https://www.bestbuy.com/site/samsung-galaxy-s22-ultra-128gb-unlocked-burgundy/6494426.p?skuId=6494426&intl=nosplash"
			//"https://www.bonanza.com/items/like/599307266/nikon-digital-camera"
			//"https://www.etsy.com/listing/624453534/ram-horn-shofar-trumpet-from-israel?ref=hp_prn-2&bes=1"
			//"https://www.lidl.com/products/1070120"
			//"https://www.target.com/p/hp-pavilion-15-6-34-gaming-laptop-intel-core-i5-10300h-qc-8gb-ram-256gb-ssd-nvidia-gtx-1650-backlit-kb-black-15-dk1045tg/-/A-83180151#lnk=sametab"
			//"https://www.vidaxl.com/e/vidaxl-speaker-stands-2-pcs-tempered-glass-2-pillars-design-silver/8718475592884.html"
			//"https://www.walmart.com/ip/Star-Wars-Grogu-Plush-Toy-11-in-The-Child-Character-from-The-Mandalorian/863451787?athcpid=863451787&athpgid=AthenaHomepageDesktop__gm__-1.0&athcgid=null&athznid=BestSellers_BestsellinginToysandGames&athieid=v0&athstid=CS020&athguid=_ZQMfaTZrZAxyLCfx9GaMlKGdOn3dMD21Lgk&athancid=null&athena=true"
			
	};

	public static void main(String[] args) throws InterruptedException {
		Platform platform = new Platform();
		platform.setDomain("Solo Test");
		platform.setCountry("United States");

		for (String url: urls) {
			new Thread(new Runnable() {

				@Override
				public void run() {
		  		Link link = new Link(url);
		  		link.setId(1l);
		  		link.setRetry(1);
		  		link.setPlatform(platform);
		  		link.setPlatformId(platform.getId());

		  		AbstractWebsite website = new WalmartXX();
		  		website.check(link);
		  		printout(link);
				}
			}).start();
		}
	}

	public static void main_1(String[] args) throws InterruptedException {
		Platform platform = new Platform();
		platform.setDomain("Solo Test");
		platform.setCountry("Canada");

		for (String url: urls) {
			new Thread(new Runnable() {

				@Override
				public void run() {
		  		Link link = new Link(url);
		  		link.setId(1l);
		  		link.setRetry(1);
		  		link.setPlatform(platform);
		  		link.setPlatformId(platform.getId());

		  		WalmartXX website = new WalmartXX();
		  		
		  		try(FileInputStream inputStream = new FileInputStream("/home/mdpinar/tmp/walmart-ca.html")) {     
		  	    String html = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
			  		website.startParsing(link, html);
			  		//website.read(link, LinkStatus.TOBE_CLASSIFIED);
			  		printout(link);
		  		} catch (Exception e) {
						e.printStackTrace();
					}		  		
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
