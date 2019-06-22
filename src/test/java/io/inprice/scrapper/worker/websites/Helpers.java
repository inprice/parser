package io.inprice.scrapper.worker.websites;

public class Helpers {

    public static String getHtmlPath(String website, String countryCode, int no) {
        return String.format("websites/%s/%s_%d.html", countryCode, website, no);
    }

    public static String getJsonPath(String website, String countryCode, int no) {
        return String.format("websites/%s/%s_%d.json", countryCode, website, no);
    }

}
