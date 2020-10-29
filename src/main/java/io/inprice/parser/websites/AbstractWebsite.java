package io.inprice.parser.websites;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;
import io.inprice.common.helpers.Beans;
import io.inprice.common.helpers.SqlHelper;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.Global;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.helpers.UserAgents;
import kong.unirest.HttpResponse;

public abstract class AbstractWebsite implements Website {

  protected static final Logger log = LoggerFactory.getLogger(AbstractWebsite.class);

  protected HttpClient httpClient = Beans.getSingleton(HttpClient.class);
  private Link link;

  protected Document doc;
  protected JSONObject json;

  @Override
  public void check(Link link) {
    this.link = link;
    long startTime = System.currentTimeMillis();

    if (willHtmlBePulled()) {
      openPage();
      if (link.getProblem() == null) read();
    } else {
      read();
    }

    log.debug("Website: {}, LinkStatus: {}, Time: {}", 
      link.getWebsiteClassName(), link.getStatus(), (System.currentTimeMillis() - startTime));
  }

  public boolean willHtmlBePulled() {
    return true;
  }

  @Override
  public String getUrl() {
    if (link == null) link = getTestLink();
    return link.getUrl();
  }

  @Override
  public Link test(String fileName) {
    return test(fileName, null);
  }

  @Override
  public Link test(String fileName, HttpClient httpClient) {
    this.link = getTestLink();

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

  protected Link getTestLink() {
    return new Link();
  }

  protected String getAlternativeUrl() {
    return null;
  }

  protected JSONObject getJsonData() {
    return null;
  }

  protected List<LinkSpec> getValueOnlySpecList(Elements specs) {
    return getValueOnlySpecList(specs, null);
  }

  protected List<LinkSpec> getValueOnlySpecList(Elements specs, String sep) {
    List<LinkSpec> specList = null;
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        if (StringUtils.isNotBlank(spec.text())) {
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

  protected String fixQuotes(String raw) {
    return raw.replaceAll("((?<=(\\{|\\[|\\,|:))\\s*')|('\\s*(?=(\\}|(\\])|(\\,|:))))", "\"");
  }

  protected void setLinkStatus(LinkStatus status) {
    link.setStatus(status);
  }

  protected LinkStatus getLinkStatus() {
    return link.getStatus();
  }

  protected void setLinkStatus(String problem) {
    link.setStatus(LinkStatus.NETWORK_ERROR);
    link.setProblem(problem);
    log.error("{}!", problem);
  }

  protected void setLinkStatus(HttpResponse<String> response) {
    link.setStatus(LinkStatus.NETWORK_ERROR);
    if (response != null) {
      link.setProblem("DATA FETCHING ERROR");
      link.setHttpStatus(response.getStatus());
      log.error("{}! Http Status: {}", link.getProblem(), response.getStatus());
    } else {
      link.setProblem("EMPTY RESPONSE");
      log.error("{}!", link.getProblem());
    }
  }

  protected void openPage() {
    String url = getAlternativeUrl();
    if (url == null) {
      url = getUrl();
    }

    String problem = null;
    int httpStatus = 200;

    try {
      Connection.Response 
      response = 
        Jsoup
          .connect(url)
          .headers(Global.standardHeaders)
          .userAgent(UserAgents.findARandomUA())
          .referrer(UserAgents.findARandomReferer())
          .timeout(SysProps.HTTP_CONNECTION_TIMEOUT() * 1000)
          .followRedirects(true)
        .execute();
      response.charset("UTF-8");

      doc = response.parse();
      link.setProblem(null);
      link.setHttpStatus(200);

    } catch (HttpStatusException hse) {
      log.error(hse.getMessage() + " -> " + url);
      problem = hse.getMessage();
      httpStatus = hse.getStatusCode();
    } catch (Exception e) {
      log.error(e.getMessage() + " -> " + url);
      problem = e.getMessage();
      httpStatus = 502;
    }

    if (problem != null) {
      link.setProblem(problem);
      link.setStatus(LinkStatus.NETWORK_ERROR);
      link.setHttpStatus(httpStatus);
    }
  }

  private String fixLength(String val, int limit) {
    if (val == null) return null;
    String newForm = EmojiParser.removeAllEmojis(fixQuotes(val)).trim();
    if (StringUtils.isNotBlank(newForm) && newForm.length() > limit)
      return SqlHelper.clear(newForm.substring(0, limit));
    else
      return SqlHelper.clear(newForm);
  }

  private void read() {
    json = getJsonData();

    // getJsonData method may return a network or socket error. thus, we need to check if it is so
    if (LinkStatus.NETWORK_ERROR.equals(link.getStatus())) {
      return;
    }

    // price settings
    BigDecimal price = getPrice();
    link.setPrice(price.setScale(2, RoundingMode.HALF_UP));

    if ((price == null || price.compareTo(BigDecimal.ONE) <= 0)
    && (getName() == null || Consts.Words.NOT_AVAILABLE.equals(getName()))) {

      LinkStatus preStatus = link.getStatus();
      if (LinkStatus.AVAILABLE.equals(preStatus)) {
        link.setStatus(LinkStatus.NETWORK_ERROR);
        link.setProblem("SOCKET ERROR");
        link.setHttpStatus(0);
      } else {
        link.setStatus(LinkStatus.NO_DATA);
      }
      log.warn("URL: " + getUrl());
      log.warn(" - Status: {}, Pre.Status: {}", link.getStatus().name(), preStatus.name());
      return;
    }

    // other settings
    link.setSku(fixLength(getSku(), Consts.Limits.SKU));
    link.setName(fixLength(getName(), Consts.Limits.NAME));
    link.setBrand(fixLength(getBrand(), Consts.Limits.BRAND));
    link.setSeller(fixLength(getSeller(), Consts.Limits.SELLER));
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

    if (isAvailable()) {
      link.setStatus(LinkStatus.AVAILABLE);
    } else {
      link.setStatus(LinkStatus.NOT_AVAILABLE);
      link.setProblem("INSUFFICIENT STOCK");
    }
  }

}
