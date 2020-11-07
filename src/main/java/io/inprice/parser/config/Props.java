package io.inprice.parser.config;

import io.inprice.common.utils.NumberUtils;

public class Props {

  public static String PROXY_HOST() {
    return System.getenv().getOrDefault("PROXY_HOST", "");
  }

  public static String PROXY_PORT() {
    return System.getenv().getOrDefault("PROXY_PORT", "");
  }

  public static int ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY() {
    return NumberUtils.toInteger(System.getenv().getOrDefault("ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY", "4"));
  }

}
