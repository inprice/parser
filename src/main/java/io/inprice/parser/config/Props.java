package io.inprice.parser.config;

import io.inprice.common.utils.NumberUtils;

public class Props {

  public static String PROXY_HOST() {
    return System.getenv().get("PROXY_HOST");
  }

  public static Integer PROXY_PORT() {
    return NumberUtils.toInteger(System.getenv().get("PROXY_PORT"));
  }

  public static String PROXY_USERNAME() {
    return System.getenv().get("PROXY_USERNAME");
  }

  public static String PROXY_PASSWORD() {
    return System.getenv().get("PROXY_PASSWORD");
  }

  public static int ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY() {
    return NumberUtils.toInteger(System.getenv().getOrDefault("ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY", "4"));
  }

}
