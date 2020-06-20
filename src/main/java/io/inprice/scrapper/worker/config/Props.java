package io.inprice.scrapper.worker.config;

public class Props {

  public static String PROXY_HOST() {
    return System.getenv().getOrDefault("PROXY_HOST", "http://mdumlupinar:f2mvm4wMBeuBC2xP@proxy.packetstream.io");
  }

  public static String PROXY_PORT() {
    return System.getenv().getOrDefault("PROXY_PORT", "31112");
  }

}
