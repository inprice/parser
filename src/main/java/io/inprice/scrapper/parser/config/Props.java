package io.inprice.scrapper.parser.config;

public class Props {

  public static String PROXY_HOST() {
    return System.getenv().getOrDefault("PROXY_HOST", "");
  }

  public static String PROXY_PORT() {
    return System.getenv().getOrDefault("PROXY_PORT", "");
  }

}
