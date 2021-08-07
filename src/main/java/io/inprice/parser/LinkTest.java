package io.inprice.parser;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.es.Gigas101;
import io.inprice.parser.websites.es.UlaBox;

public class LinkTest {
	
	/**
	 * Australia
	 * appliancesonline, bigw, harveynorman, kogan, thegoodguys
	 * 
	 */
	
	/**
	 * Canada
	 * bestbuy, canadiantire, 
	 * walmart(over singly queue)
	 * 
	 */

	/**
	 * Germany
	 * euronics, mediamarkt, notebooksbillinger, otto
	 * 
	 */

	/**
	 * Spain
	 * electroking, euronics, 101gigas, ulabox
	 * 
	 */

	private static String[] urls = {
		"https://www.ulabox.com/en/product/ambientador-air-wick-freshmatic-nenuco-recambio/17990?ula_src=front_category_show&ula_mdm=product_list",
		"https://www.ulabox.com/en/lanostrapasta/product/pan-seco-crujiente-mini-lingua-di-suocera-mario-fongo-100g/91820?ula_src=front_index&ula_mdm=product_list",
		"https://www.ulabox.com/en/product/cristalinas-ambientador-aroma-de-mora-mikado-40ml/79295?ula_src=front_category_show&ula_mdm=product_list",
		"https://www.ulabox.com/en/product/pina-en-rodajas-en-su-jugo-del-monte/53641?ula_src=front_category_show&ula_mdm=product_list",
		"https://www.ulabox.com/en/vinos-el-petit-celler/product/vino-tinto-abadal-matis-2017/94601?ula_src=front_index&ula_mdm=product_list"
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

		  		Website website = new UlaBox();
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
