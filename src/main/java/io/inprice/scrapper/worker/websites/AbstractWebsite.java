package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;

public abstract class AbstractWebsite extends AbstractSite {

    protected static final Logger log = new Logger(AbstractWebsite.class);

    protected AbstractWebsite(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        return null;
    }

    protected String cleanPrice(String price) {
        StringBuilder sb = new StringBuilder();
        for (Character ch: price.toCharArray()) {
            if ((ch >= '0' && ch <= '9') || ch == ',' || ch == '.') sb.append(ch);
        }
        String trimmed = sb.toString();
        boolean commaDecimal =  (trimmed.length() > 3 && trimmed.charAt(trimmed.length() - 3) == ',');

        String pure = trimmed.replaceAll("[^\\d.]", "").trim();

        if (commaDecimal) {
            int ix = pure.length()-2;
            return pure.substring(0, ix) + "." + pure.substring(ix);
        } else {
            return pure;
        }
    }

}
