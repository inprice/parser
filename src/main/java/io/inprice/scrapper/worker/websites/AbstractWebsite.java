package io.inprice.scrapper.worker.websites;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.helpers.Beans;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.common.utils.NumberUtils;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.helpers.UserAgents;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWebsite implements Website {

  protected static final Logger log = LoggerFactory.getLogger(AbstractWebsite.class);

  protected HttpClient httpClient = Beans.getSingleton(HttpClient.class);
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
    long startTime = System.currentTimeMillis();

    if (willHtmlBePulled()) {
      createDoc();
      if (link.getHttpStatus() == null || link.getHttpStatus() == 200)
        read();
    } else {
      read();
    }

    log.debug("Website: {}, Status: {}, Time: {}", link.getWebsiteClassName(), link.getStatus(),
        (System.currentTimeMillis() - startTime));
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
    return test(fileName, null);
  }

  @Override
  public Link test(String fileName, HttpClient httpClient) {
    if (httpClient != null)
      this.httpClient = httpClient;
    try {
      if (willHtmlBePulled()) {
        URL path = ClassLoader.getSystemResource(fileName);
        File input = new File(path.toURI());
        doc = Jsoup.parse(input, "UTF-8");
      }
      read();
    } catch (Exception e) {
      log.error("Failed to fetch the page during test!", e);
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
          LinkSpec ls = new LinkSpec("", spec.text());
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
          specList.add(
              new LinkSpec((key != null ? key.text().replaceAll(":", "") : ""), (value != null ? value.text() : "")));
        }
      }
    }
    return specList;
  }

  protected String cleanDigits(String numString) {
    return NumberUtils.extractPrice(numString);
  }

  protected String findAPart(String html, String starting, String ending) {
    return findAPart(html, starting, ending, 0);
  }

  protected String findAPart(String html, String starting, String ending, int plus) {
    int start = html.indexOf(starting) + starting.length();
    int end = html.indexOf(ending, start) + plus;

    if (start > starting.length() && end > start) {
      return html.substring(start, end);
    }

    return null;
  }

  private String fixLength(String val, int limit) {
    String newForm = fixQuotes(val.trim());
    if (!newForm.isEmpty() && newForm.length() > limit)
      return newForm.substring(0, limit);
    else
      return newForm;
  }

  protected String fixQuotes(String raw) {
    return raw.replaceAll("((?<=(\\{|\\[|\\,|:))\\s*')|('\\s*(?=(\\}|(\\])|(\\,|:))))", "\"");
  }

  private void read() {
    Status previousStatus = link.getStatus();
    json = getJsonData();

    if (!link.getStatus().equals(previousStatus)) {
      // getJsonData method may return a network or socket error. thus, we need to
      // check if it is so
      if (Status.READ_ERROR.equals(link.getStatus()) || Status.NO_DATA.equals(link.getStatus())
          || Status.SOCKET_ERROR.equals(link.getStatus()) || Status.NETWORK_ERROR.equals(link.getStatus())) {
        return;
      }
    }

    // price settings
    BigDecimal price = getPrice().setScale(2, RoundingMode.HALF_UP);
    link.setPrice(price);
    if ((getPrice() == null || getPrice().compareTo(BigDecimal.ONE) < 0)
        && (getName() == null || Consts.Words.NOT_AVAILABLE.equals(getName()))) {
      link.setStatus(Status.NOT_A_PRODUCT_PAGE);
      log.warn("URL doesn't point at a specific page! " + getUrl());
      return;
    }

    // other settings
    if (Status.NEW.equals(link.getStatus()) || Status.RENEWED.equals(link.getStatus())) {
      if (getSku() != null)
        link.setSku(fixLength(getSku(), Consts.Limits.SKU));
      if (getName() != null)
        link.setName(fixLength(getName(), Consts.Limits.NAME));
      if (getBrand() != null)
        link.setBrand(fixLength(getBrand(), Consts.Limits.BRAND));
      if (getSeller() != null)
        link.setSeller(fixLength(getSeller(), Consts.Limits.SELLER));
      if (getShipment() != null)
        link.setShipment(fixLength(getShipment(), Consts.Limits.SHIPMENT));

      // spec list editing
      List<LinkSpec> specList = getSpecList();
      if (specList != null && specList.size() > 0) {
        List<LinkSpec> newList = new ArrayList<>(specList.size());
        for (LinkSpec ls : specList) {
          newList.add(new LinkSpec(fixLength(ls.getKey(), Consts.Limits.SPEC_KEY),
              fixLength(ls.getValue(), Consts.Limits.SPEC_VALUE)));
        }
        link.setSpecList(newList);
      }
    }

    if (isAvailable()) {
      link.setStatus(Status.AVAILABLE);
    } else {
      link.setStatus(Status.NOT_AVAILABLE);
      log.debug("Link with id {} is not available!", link.getId());
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
      log.error("Failed to fetch data! Status: {}, Http Status: {}", status.name(), response.getStatus());
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
    }
    if (httpStatus > 399) {
      link.setStatus(Status.NETWORK_ERROR);
      link.setHttpStatus(httpStatus);
    }
  }

  protected int openDocument() {
    String url = getAlternativeUrl();
    if (url == null)
      url = getUrl();

    try {
      Connection.Response response = Jsoup.connect(url).headers(Global.standardHeaders)
          .userAgent(UserAgents.findARandomUA()).referrer(UserAgents.findARandomReferer()).timeout(5 * 1000)
          .ignoreContentType(true).followRedirects(true).execute();
      doc = response.parse();
      return response.statusCode();
    } catch (HttpStatusException httpe) {
      log.error(httpe.getMessage() + " : " + url);
      return httpe.getStatusCode();
    } catch (IOException e) {
      log.error(e.getMessage() + " : " + url);
      return 0;
    }
  }

}
