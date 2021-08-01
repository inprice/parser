package io.inprice.parser;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.config.Props;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.au.BigW;

public class LinkTest {
	
	/**
	 * Turkey
	 * trendyol, teknosa, amazon, MediaMarkt
	 * N11, Gittitgidiyor, hepsiburada
	 */
	
	/**
	 * Australia
	 * appliancesonline, bigw
	 * 
	 */

	private static Website website = new BigW();
	private static String[] urls = {
			"https://www.bigw.com.au/product/tradie-men-s-work-boots-wheat/p/1193138-wheat/",
			"https://www.bigw.com.au/product/craftsmart-low-temperature-mini-glue-gun/p/110978/",
			"https://www.bigw.com.au/product/repco-little-monsta-30cm-kids-bmx-coaster-bike/p/474961/",
			"https://www.bigw.com.au/product/bluey-balance-bike/p/94058/",
			"https://www.bigw.com.au/product/digimon-x-digivice-white-blue/p/164006/",
		};

	public static void main(String[] args) {
		//Global.initWebDriver();
//		setProxy();

		Platform platform = new Platform();
		platform.setName("Test Site");
		platform.setDomain("Solo Test");

		for (String url: urls) {
  		Link link = new Link(url);
  		link.setId(1l);
  		link.setRetry(1);
  		link.setPlatform(platform);
  		link.setPlatformId(platform.getId());
  		
  		website.check(link);
  		printout(link);
		}
/*
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) { }
		
		Global.closeWebDriver();
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
	
	private static void setProxy() {
    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

    Authenticator.setDefault(new Authenticator() {
    	@Override
      protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType().equals(RequestorType.PROXY)) {
          return new PasswordAuthentication(Props.PROXY_USERNAME, Props.PROXY_PASSWORD.toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });
	}
	
}
