package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.us.LidlUS;
import io.inprice.parser.websites.us.WalmartUS;

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
	// U.Kingdom --> argos, asdadirect, asdagrocery, asos, currys, debenhams, euronics, newlook, zavvi
	// U.States  --> bestbuy, bonanza, etsy, walmart

	private static String[] urls = {
		"https://www.walmart.com/ip/Lenovo-Chromebook-S330-14-Mediatek-MT8173C-4GB-32GB-eMMC-SSD-Chromebook-Black-81JW0001US/206750547",
		"https://www.walmart.com/ip/Mighty-Rock-Vr-Headset-3D-Glasses-Virtual-Reality-Headset-for-VR-Games-3D-Movies-Eye-Care-System-for-iPhone-and-Android-Smartphones/492095578",
		"https://www.walmart.com/ip/One-Piece-Series-94-Wheel-20x10-Black-w-Milled-Accents-6x5-5-6x135-94201012-Rough-Country/214001352",
		"https://www.walmart.com/ip/TP-Link-TL-WN725N-150Mbps-Wireless-N-Nano-USB-Adapter-Speedy-Wireless-Transmission/21553789",
		"https://www.walmart.com/ip/Walnew-4-PCS-Outdoor-Patio-Furniture-Brown-PE-Rattan-Wicker-Table-and-Chairs-Set/653423349?wpa_bd=&wpa_pg_seller_id=E2AA6F796EA3489D86EB52E4E6A149AE&wpa_ref_id=wpaqs:WwnsLsLGDSO7n9zLcreSTY9RVHF7AyipXeAM84SR-rhxInqm4N-CoU1nAED3iXSXcNWXOrMJl8yJVaV278RkFGGcGPDVnhjum2ICysNlYvqae2sxYWcHmIxIHcjXysaxitDTx4Hk3-mn9BV2fxygt-On9uYwPjTNIrUDeyycavBhm2Pjto2UwyyPcqYmqjpizvutgEcxFvKSYet36H1sSQ&wpa_tag=&wpa_aux_info=&wpa_pos=2&wpa_plmt=1145x1145_T-C-IG_TI_1-6_HL-INGRID-GRID-NY&wpa_aduid=6046cecb-eee8-4397-a7a1-5d432cb0dd13&wpa_pg=browse&wpa_pg_id=5428_91416_3013177&wpa_st=__searchterms__&wpa_tax=5428_91416_3013177&wpa_bucket=__bkt__"
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

		  		Website website = new WalmartUS();
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
