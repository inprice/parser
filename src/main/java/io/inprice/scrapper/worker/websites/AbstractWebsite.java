package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractWebsite implements Website {

    private static final Logger log = new Logger(AbstractWebsite.class);

    protected Document doc;
    protected JSONObject json;

    @Override
    public void check(Link link) {
        createDoc(link);

        json = getJsonData();

        if (Status.NEW.equals(link.getStatus()) || Status.RENEW.equals(link.getStatus()) || link.getName() == null) {
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
            String deliveryMessage = getDeliveryMessage();
            if (deliveryMessage != null) link.setShipment(deliveryMessage);
            link.setStatus(Status.UNAVAILABLE);
            log.debug("This product is now unavailable!");
        }
    }

    public JSONObject getJsonData() {
        return null;
    }

    public String getDeliveryMessage() {
        return null;
    }

    protected String cleanPrice(String price) {
        String trimmed = price.trim();
        boolean commaDecimal =  (trimmed.length() > 3 && trimmed.charAt(trimmed.length() - 3) == ',');

        String pure = trimmed.replaceAll("[^\\d.]", "").trim();

        if (commaDecimal) {
            int ix = pure.length()-2;
            return pure.substring(0, ix) + "." + pure.substring(ix);
        } else {
            return pure;
        }
    }

    private void createDoc(Link link) {
        int httpStatus = openDocument(link.getUrl());

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
                    .timeout(5000)
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
