package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigDecimal;

public abstract class AbstractWebsite implements Website {

    private static final Logger log = new Logger(AbstractWebsite.class);

    protected Document doc;

    @Override
    public void check(Link link) {
        createDoc(link);

        if (LinkStatus.NEW.equals(link.getStatus()) || LinkStatus.ACTIVE.equals(link.getStatus())) {
            if (LinkStatus.NEW.equals(link.getStatus())) {
                link.setStatus(LinkStatus.ACTIVE);
                link.setTitle(getTitle());
                link.setCode(getCode());
                link.setSeller(getSeller());
                link.setShipment(getShipment());
                link.setBrand(getBrand());
                link.setSpecList(getSpecList());
            }

            if (isAvailable()) {
                BigDecimal price = getPrice();
                if (!price.equals(link.getPrice())) {
                    sendAPriceChangeMessage(link, price);
                    link.setPrice(getPrice());
                }

                log.debug("Price : %f, Seller: %s, Shipment: %s, Brand: %s", link.getPrice(), link.getSeller(), link.getShipment(), link.getBrand());
            } else {
                link.setStatus(LinkStatus.UNAVAILABLE);
                log.debug("This product is now unavailable!");
            }

        } else {
            sendAStatusChangeMessage(link);
            log.warn("URL Problem [%s] - %s", link.getStatus().name(), link.getUrl());
        }
    }

    private void sendAPriceChangeMessage(Link link, BigDecimal newPrice) {

    }

    private void sendAStatusChangeMessage(Link link) {

    }

    protected String cleanPrice(String price) {
        return price.replace(",", ".").replaceAll("[^\\d.]", "").trim();
    }

    private void createDoc(Link link) {
        int httpStatus = openDocument(link.getUrl());

        if (httpStatus == 0) {
            link.setStatus(LinkStatus.SOCKET_ERROR);
        } if (httpStatus > 399) {
            link.setStatus(LinkStatus.NETWORK_ERROR);
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
