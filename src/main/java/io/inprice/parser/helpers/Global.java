package io.inprice.parser.helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

public class Global {

  public static volatile boolean isApplicationRunning;
  private static WebDriver webDriver;

  public static void initWebDriver() {
  	ProfilesIni profileIni = new ProfilesIni();
		FirefoxProfile profile = profileIni.getProfile("scrapper");
		
		FirefoxOptions capabilities = new FirefoxOptions();
		//capabilities.setCapability(FirefoxDriver.PROFILE, fireFoxProfile);

		webDriver = new FirefoxDriver(capabilities);
  }

  public static void closeWebDriver() {
  	webDriver.quit();
  }
  
  public static WebDriver getWebDriver() {
		return webDriver;
	}

}
