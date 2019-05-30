package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSite implements Website {

    protected static final Logger log = new Logger(AbstractSite.class);

    private Link link;

    protected Document doc;
    protected JSONObject json;

    AbstractSite(Link link) {
        this.link = link;
    }

    protected abstract JSONObject getJsonData();

    @Override
    public void check() {
        createDoc();
        if (link.getHttpStatus() == null || link.getHttpStatus() == 200) read();
    }

    @Override
    public String getMainUrl() {
        return link.getUrl();
    }

    public String getSubUrl() {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean willHtmlBeDownloaded() {
        return true;
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
        String url = getSubUrl();
        if (url == null) url = getMainUrl();

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
}
