package io.inprice.scrapper.worker.websites;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
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

    public boolean willHtmlBePulled() {
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

    protected String getAlternativeUrl() {
        return null;
    }

    @Override
    public Link test(String fileName) {
        try {
            if (willHtmlBePulled()) {
                URL path = ClassLoader.getSystemResource(fileName);
                File input = new File(path.toURI());
                doc = Jsoup.parse(input, "UTF-8");
            }
            read();
        } catch (Exception e) {
            log.error(e);
        }
        return link;
    }

    protected List<LinkSpec> getValueOnlySpecList(Elements specs) {
       return getValueOnlySpecList(specs, null);
    }

    protected List<LinkSpec> getValueOnlySpecList(Elements specs, String sep) {
        List<LinkSpec> specList = null;
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                if (!spec.text().trim().isEmpty()) {
                    LinkSpec ls = new LinkSpec("", spec.text().trim());
                    if (sep != null && ls.getValue().indexOf(sep) > 0) {
                        String[] specChunks = ls.getValue().split(sep);
                        ls.setKey(specChunks[0]);
                        ls.setValue(specChunks[1]);
                    }
                    specList.add(ls);
                }
            }
        }
        return specList;
    }

    protected List<LinkSpec> getKeyValueSpecList(Elements specs, String keySelector, String valueSelector) {
        List<LinkSpec> specList = null;
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                Element key = spec.selectFirst(keySelector);
                Element value = spec.selectFirst(valueSelector);
                if (key != null || value != null) {
                    specList.add(new LinkSpec((key != null ? key.text().replaceAll(":", "") : ""), (value != null ? value.text() : "")));
                }
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

        /*
         * getJsonData method may return a network or socket error. thus, we need to check if it is so
         */
        if (Status.READ_ERROR.equals(link.getStatus())
        ||  Status.NO_DATA.equals(link.getStatus())
        ||  Status.SOCKET_ERROR.equals(link.getStatus())
        ||  Status.NETWORK_ERROR.equals(link.getStatus())) {
            return;
        }

        if (link.getName() == null
        ||  Status.NEW.equals(link.getStatus())
        ||  Status.RENEWED.equals(link.getStatus())) {
            link.setSku(getSku());
            link.setName(getName());
            link.setSeller(getSeller());
            link.setShipment(getShipment());
            link.setBrand(getBrand());
            link.setSpecList(getSpecList());
        }

        BigDecimal price = getPrice().setScale(2, RoundingMode.HALF_UP);
        link.setPrice(price);

        if ((getPrice() == null || getPrice().compareTo(BigDecimal.ONE) < 0) && (getName() == null || Constants.NOT_AVAILABLE.equals(getName()))) {
            link.setStatus(Status.NOT_SPECIFIC);
            log.warn("URL doesn't point at a specific page! " + getUrl());
            return;
        }

        if (isAvailable()) {
            link.setStatus(Status.AVAILABLE);
        } else {
            link.setStatus(Status.OUT_OF_STOCK);
            log.debug("This product is not available!");
        }
    }

    protected Status getLinkStatus() {
        return link.getStatus();
    }

    protected void setLinkStatus(Status status) {
        link.setStatus(status);
    }

    protected void setLinkStatus(Status status, int httpStatus) {
        link.setStatus(status);
        link.setHttpStatus(httpStatus);
    }

    protected void setLinkStatus(HttpResponse<String> response) {
        if (response != null) {
            final Status status = (response.getStatus() == 0 ? Status.SOCKET_ERROR : Status.NETWORK_ERROR);
            log.error("Failed to fetch data! Status: %s, Http Status: %d", status.name(), response.getStatus());
            setLinkStatus(status, response.getStatus());
        } else {
            log.error("Response is null!");
            setLinkStatus(Status.READ_ERROR);
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

    protected int openDocument() {
        String url = getAlternativeUrl();
        if (url == null) url = getUrl();

        try {
            Connection.Response response =
                Jsoup.connect(url)
                    .headers(Global.standardHeaders)
                    .userAgent(UserAgents.findARandomUA())
                    .referrer(UserAgents.findARandomReferer())
                    .timeout(7 * 1000)
                    .ignoreContentType(true)
                    .followRedirects(false)
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
