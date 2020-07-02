package io.inprice.parser.browser;

public class BrowserManager {

  private static AbstractBrowser harveyNormanBrowser;

  static {
    harveyNormanBrowser = new HarveyNormanBrowser();
  }

  public static AbstractBrowser getHarveyNormanBrowser() {
    return harveyNormanBrowser;
  }

  public static void closeAllBrowsers() {
    harveyNormanBrowser.quit();
  }

}
