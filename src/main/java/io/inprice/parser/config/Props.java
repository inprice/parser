package io.inprice.parser.config;

import io.inprice.common.utils.NumberUtils;

public class Props {

  public static final String WEBDRIVER_URL;
  public static final String PROXY_HOST;
  public static final Integer PROXY_PORT;
  public static final String PROXY_USERNAME;
  public static final String PROXY_PASSWORD;

  static {
  	//WEBDRIVER_URL = System.getenv().getOrDefault("WEBDRIVER_URL", "http://127.0.0.1:9515");
		WEBDRIVER_URL = System.getenv().getOrDefault("WEBDRIVER_URL", "http://127.0.0.1:4444");

		PROXY_HOST = System.getenv().getOrDefault("PROXY_HOST", "proxy.packetstream.io");
  	PROXY_PORT = NumberUtils.toInteger(System.getenv().getOrDefault("PROXY_PORT", "31112"));
  	PROXY_USERNAME = System.getenv().getOrDefault("PROXY_USERNAME", "mdumlupinar");
  	PROXY_PASSWORD = System.getenv().getOrDefault("PROXY_PASSWORD", "ZLLKIoebz0Xi1WVk");
  }

}
