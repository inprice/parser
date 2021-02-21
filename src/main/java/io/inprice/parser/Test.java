package io.inprice.parser;

import io.inprice.common.utils.NumberUtils;

public class Test {
  
  public static void main(String[] args) {
  	String[] vals = {"timedout", "TIMED OUT", "tImEOuT", "tImEdOuT", "hasan", "veli"};
  	for (String val : vals) {
  		System.out.println(val + ": " + val.matches("(?i)time.*out"));
		}
  }

}