package io.inprice.scrapper.worker.helpers;

import java.util.HashMap;
import java.util.Map;

public class Global {

    public static volatile boolean isApplicationRunning;

    public static Map<String, String> standardHeaders;

    static {
        standardHeaders = new HashMap<>();
        standardHeaders.put("Accept-Language", "en-US,en;q=0.5");
        standardHeaders.put("Cache-Control","max-age=0");
    }

}
