package io.inprice.parser.helpers;

import org.openqa.selenium.chrome.ChromeOptions;

public class ProxyHelper {
	
	public static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();
  	options.addArguments(
  			"--headless", 
  			"--disable-gpu", 
  			"--window-size=1920,1200",
  			"--ignore-certificate-errors", 
  			"--disable-popup-blocking", 
  			"--blink-settings=imagesEnabled=false",
				"--user-agent=" + UserAgents.getRandomUserAgent()
			);
		return options;
	}
	
}
