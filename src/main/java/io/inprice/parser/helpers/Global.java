package io.inprice.parser.helpers;

import io.inprice.parser.pool.WebClientPool;

public class Global {

  public static volatile boolean isApplicationRunning;
  
  public static WebClientPool WEB_CLIENT_POOL = new WebClientPool();

}
