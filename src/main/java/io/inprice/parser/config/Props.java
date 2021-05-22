package io.inprice.parser.config;

import io.inprice.common.utils.NumberUtils;

public class Props {

  public static final String WEBDRIVER_URL;
  public static final String PROXY_HOST;
  public static final Integer PROXY_PORT;
  public static final String PROXY_USERNAME;
  public static final String PROXY_PASSWORD;
  
  static {
		WEBDRIVER_URL = System.getenv().getOrDefault("WEBDRIVER_URL", "http://127.0.0.1:9515");
  	PROXY_HOST = System.getenv().get("PROXY_HOST");
  	PROXY_PORT = NumberUtils.toInteger(System.getenv().get("PROXY_PORT"));
  	PROXY_USERNAME = System.getenv().get("PROXY_USERNAME");
  	PROXY_PASSWORD = System.getenv().get("PROXY_PASSWORD");
  }

}
