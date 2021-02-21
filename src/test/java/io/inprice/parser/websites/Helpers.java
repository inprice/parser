package io.inprice.parser.websites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Helpers {

  public static String getHtmlPath(Website website, int no) {
    //return String.format("websites/%s/%s_%d.html", website.getCountry().getCode(), website.getSiteName(), no);
  	return "";
  }

  public static String getEmptyHtmlPath() {
    return "websites/exceptions/empty.html";
  }

  public static String readFile(String fileName) {
    StringBuilder data = new StringBuilder();
    try (InputStream inputStream = Helpers.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader)) {

      String line;
      while ((line = reader.readLine()) != null) {
        data.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data.toString();
  }

}
