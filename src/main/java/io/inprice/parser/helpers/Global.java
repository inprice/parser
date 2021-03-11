package io.inprice.parser.helpers;

import java.util.HashMap;
import java.util.Map;

public class Global {

  public static volatile boolean isApplicationRunning;
  
  public static Map<String, String> standardHeaders;

  static {
    standardHeaders = new HashMap<>(5);
    standardHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    standardHeaders.put("Accept-Language", "en-US,en;q=0.5");
    standardHeaders.put("Accept-Encoding", "gzip, deflate, br");
    standardHeaders.put("Connection", "keep-alive");
    standardHeaders.put("TE", "Trailers");
    
  }

}
