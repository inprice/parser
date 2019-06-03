package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWebsite implements Website {

    protected static final Logger log = new Logger(AbstractWebsite.class);

    private Link link;

    protected Document doc;
    protected JSONObject json;

    protected AbstractWebsite(Link link) {
        this.link = link;
    }

    protected boolean willHtmlBePulled() {
        return true;
    }

    protected JSONObject getJsonData() {
        return null;
    }

    @Override
    public void check() {
        if (willHtmlBePulled()) {
            createDoc();
            if (link.getHttpStatus() == null || link.getHttpStatus() == 200) read();
        } else {
            read();
        }
    }

    @Override
    public String getUrl() {
        return link.getUrl();
    }

    @Override
    public String getAlternativeUrl() {
        return null;
    }

    @Override
    public Link test(String fileName) {
        return null;
    }

    protected List<LinkSpec> getValueOnlySpecList(Elements specs) {
        List<LinkSpec> specList = null;
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                specList.add(new LinkSpec("", spec.text().trim()));
            }
        }
        return specList;
    }

    protected List<LinkSpec> getKeyValueSpecList(Elements specs, String keySelector, String valueSelector) {
        List<LinkSpec> specList = null;
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String key = spec.select(keySelector).text();
                String value = spec.select(valueSelector).text();
                specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
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

    private void read() {
        json = getJsonData();

        if (Status.NEW.equals(link.getStatus()) || Status.RENEWED.equals(link.getStatus()) || link.getName() == null) {
            link.setSku(getSku());
            link.setName(getName());
            link.setSeller(getSeller());
            link.setShipment(getShipment());
            link.setBrand(getBrand());
            link.setSpecList(getSpecList());
        }

        BigDecimal price = getPrice().setScale(2, RoundingMode.HALF_UP);
        if (! price.equals(link.getPrice())) link.setPrice(price);
        log.debug("Price : %f, Seller: %s, Shipment: %s, Brand: %s", link.getPrice(), link.getSeller(), link.getShipment(), link.getBrand());

        if (isAvailable()) {
            link.setStatus(Status.ACTIVE);
        } else {
            link.setStatus(Status.UNAVAILABLE);
            log.debug("This product is not available!");
        }
    }

    private void createDoc() {
        int httpStatus = openDocument();
        if (httpStatus != 200)
            link.setHttpStatus(httpStatus);
        if (httpStatus == 0) {
            link.setStatus(Status.SOCKET_ERROR);
        } if (httpStatus > 399) {
            link.setStatus(Status.NETWORK_ERROR);
            link.setHttpStatus(httpStatus);
        }
    }

    private int openDocument() {
        String url = getAlternativeUrl();
        if (url == null) url = getUrl();

        try {
            Connection.Response response =
                Jsoup.connect(url)
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Cache-Control","max-age=0")
                    .userAgent(UserAgents.findARandomUA())
                    .referrer(UserAgents.findARandomReferer())
                    .timeout(0)
                    .ignoreContentType(true)
                    .followRedirects(true)
                .execute();
            doc = response.parse();
            return response.statusCode();
        } catch (HttpStatusException httpe) {
            log.error("Something went wrong: " + url, httpe);
            return httpe.getStatusCode();
        } catch (IOException e) {
            log.error("Failed to connect to " + url, e);
            return 0;
        }
    }

}
