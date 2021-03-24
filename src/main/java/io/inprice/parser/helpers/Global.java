package io.inprice.parser.helpers;

import io.inprice.parser.pool.HtmlUnitPool;

public class Global {

  public static volatile boolean isApplicationRunning;
  
  public static HtmlUnitPool HTMLUNIT_POOL = new HtmlUnitPool();

}
