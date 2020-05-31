package io.inprice.scrapper.worker;

import io.inprice.scrapper.common.utils.NumberUtils;

public class Test {
  
  public static void main(String[] args) {
    System.out.println(NumberUtils.extractPrice("3.453.451,88 TL "));
  }

}