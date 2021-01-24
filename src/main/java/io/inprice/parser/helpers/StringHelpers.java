package io.inprice.parser.helpers;

public class StringHelpers {
  
  public static String escapeJSON(String json) {

    return json
        .replaceAll("\n", " ")
        .replaceAll("\r", "");     
  }

}
