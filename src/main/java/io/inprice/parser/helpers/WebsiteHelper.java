package io.inprice.parser.helpers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.parser.websites.Website;

public class WebsiteHelper {

  private static final Logger log = LoggerFactory.getLogger(WebsiteHelper.class);

  private static final String PACKAGE_PATH = "io.inprice.parser.websites.";
  private static Map<String, Website> map = new HashMap<>();

  public static Website findByClassName(String className) {
    Website website = map.get(className);
    if (website == null) {
      try {
        Class<?> clazz = Class.forName(PACKAGE_PATH + className);
        website = (Website) clazz.getConstructor().newInstance();
        map.put(className, website);
      } catch (Exception e) {
        log.error("Failed to find website by class name: " + className, e);
      }
    }
    return website;
  }

}
