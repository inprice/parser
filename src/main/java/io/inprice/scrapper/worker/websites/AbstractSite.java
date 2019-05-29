package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class AbstractSite implements Website {

    protected static final Logger log = new Logger(AbstractSite.class);

    protected String url;
    protected Document doc;
    protected JSONObject json;

    protected abstract JSONObject getJsonData();

    @Override
    public void check(Link link) {
        createDoc(link);
        if (link.getHttpStatus() == null || link.getHttpStatus() == 200) read(link);
    }

    private void read(Link link) {
        this.url = link.getUrl();
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

    private void createDoc(Link link) {
        int httpStatus = openDocument(link.getUrl());
        if (httpStatus != 200)
            link.setHttpStatus(httpStatus);
        if (httpStatus == 0) {
            link.setStatus(Status.SOCKET_ERROR);
        } if (httpStatus > 399) {
            link.setStatus(Status.NETWORK_ERROR);
            link.setHttpStatus(httpStatus);
        }
    }

    private int openDocument(String url) {
        try {
            Connection.Response response =
                Jsoup.connect(url)
                        .userAgent(UserAgents.findARandomUA())
                        .referrer(UserAgents.findARandomReferer())
                        .maxBodySize(0)
                        .timeout(10000)
                        .ignoreContentType(true)
                        .followRedirects(true)
                        .execute();
            doc = response.parse();
            return response.statusCode();
        } catch (IOException e) {
            log.error("Failed to connect to " + url, e);
            return 0;
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

}
