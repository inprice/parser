package io.inprice.scrapper.worker.websites;

import java.io.File;
import java.io.IOException;
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

import io.inprice.scrapper.common.helpers.Beans;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.common.utils.NumberUtils;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.helpers.UserAgents;
import kong.unirest.HttpResponse;

public abstract class AbstractWebsite implements Website {

  protected static final Logger log = LoggerFactory.getLogger(AbstractWebsite.class);

  protected HttpClient httpClient = Beans.getSingleton(HttpClient.class);
  private Competitor competitor;

  protected Document doc;
  protected JSONObject json;

  protected AbstractWebsite(Competitor competitor) {
    this.competitor = competitor;
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
      if (competitor.getHttpStatus() == null || competitor.getHttpStatus() == 200) read();
    } else {
      read();
    }

    log.debug("Website: {}, CompetitorStatus: {}, Time: {}", competitor.getWebsiteClassName(), competitor.getStatus(),
        (System.currentTimeMillis() - startTime));
  }

  @Override
  public String getUrl() {
    return competitor.getUrl();
  }

  protected String getAlternativeUrl() {
    return null;
  }

  @Override
  public Competitor test(String fileName) {
    return test(fileName, null);
  }

  @Override
  public Competitor test(String fileName, HttpClient httpClient) {
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
    return competitor;
  }

  protected List<CompetitorSpec> getValueOnlySpecList(Elements specs) {
    return getValueOnlySpecList(specs, null);
  }

  protected List<CompetitorSpec> getValueOnlySpecList(Elements specs, String sep) {
    List<CompetitorSpec> specList = null;
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        if (StringUtils.isNotBlank(spec.text())) {
          CompetitorSpec ls = new CompetitorSpec("", spec.text());
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

  protected List<CompetitorSpec> getKeyValueSpecList(Elements specs, String keySelector, String valueSelector) {
    List<CompetitorSpec> specList = null;
    if (specs != null && specs.size() > 0) {
      specList = new ArrayList<>();
      for (Element spec : specs) {
        Element key = spec.selectFirst(keySelector);
        Element value = spec.selectFirst(valueSelector);
        if (key != null || value != null) {
          specList.add(
              new CompetitorSpec((key != null ? key.text().replaceAll(":", "") : ""), (value != null ? value.text() : "")));
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
    if (val == null) return null;
    String newForm = EmojiParser.removeAllEmojis(fixQuotes(val)).trim();
    if (StringUtils.isNotBlank(newForm) && newForm.length() > limit)
      return newForm.substring(0, limit);
    else
      return newForm;
  }

  protected String fixQuotes(String raw) {
    return raw.replaceAll("((?<=(\\{|\\[|\\,|:))\\s*')|('\\s*(?=(\\}|(\\])|(\\,|:))))", "\"");
  }

  private void read() {
    json = getJsonData();

    // getJsonData method may return a network or socket error. thus, we need to check if it is so
    if (CompetitorStatus.READ_ERROR.equals(competitor.getStatus()) || CompetitorStatus.NO_DATA.equals(competitor.getStatus())
    || CompetitorStatus.SOCKET_ERROR.equals(competitor.getStatus()) || CompetitorStatus.NETWORK_ERROR.equals(competitor.getStatus())) {
      return;
    }

    // price settings
    BigDecimal price = getPrice();
    competitor.setPrice(price.setScale(2, RoundingMode.HALF_UP));

    if ((price == null || price.compareTo(BigDecimal.ONE) <= 0)
    && (getName() == null || Consts.Words.NOT_AVAILABLE.equals(getName()))) {

      CompetitorStatus preStatus = competitor.getStatus();
      if (CompetitorStatus.AVAILABLE.equals(preStatus)) {
        competitor.setStatus(CompetitorStatus.SOCKET_ERROR);
      } else {
        competitor.setStatus(CompetitorStatus.NO_DATA);
      }
      log.warn("URL: " + getUrl());
      log.warn(" - Status: {}, Pre.Status: {}", competitor.getStatus().name(), preStatus.name());
      return;
    }

    // other settings
    competitor.setSku(fixLength(getSku(), Consts.Limits.SKU));
    competitor.setName(fixLength(getName(), Consts.Limits.NAME));
    competitor.setBrand(fixLength(getBrand(), Consts.Limits.BRAND));
    competitor.setSeller(fixLength(getSeller(), Consts.Limits.SELLER));
    competitor.setShipment(fixLength(getShipment(), Consts.Limits.SHIPMENT));

    // spec list editing
    List<CompetitorSpec> specList = getSpecList();
    if (specList != null && specList.size() > 0) {
      List<CompetitorSpec> newList = new ArrayList<>(specList.size());
      for (CompetitorSpec ls : specList) {
        newList.add(new CompetitorSpec(fixLength(ls.getKey(), Consts.Limits.SPEC_KEY),
            fixLength(ls.getValue(), Consts.Limits.SPEC_VALUE)));
      }
      competitor.setSpecList(newList);
    }

    if (isAvailable()) {
      competitor.setStatus(CompetitorStatus.AVAILABLE);
    } else {
      competitor.setStatus(CompetitorStatus.NOT_AVAILABLE);
      log.debug("Competitor with id {} is not available!", competitor.getId());
    }
  }

  protected CompetitorStatus getCompetitorStatus() {
    return competitor.getStatus();
  }

  protected void setCompetitorStatus(CompetitorStatus status) {
    competitor.setStatus(status);
  }

  protected void setCompetitorStatus(CompetitorStatus status, int httpStatus) {
    competitor.setStatus(status);
    competitor.setHttpStatus(httpStatus);
  }

  protected void setCompetitorStatus(HttpResponse<String> response) {
    if (response != null) {
      final CompetitorStatus status = (response.getStatus() == 0 ? CompetitorStatus.SOCKET_ERROR : CompetitorStatus.NETWORK_ERROR);
      log.error("Failed to fetch data! CompetitorStatus: {}, Http CompetitorStatus: {}", status.name(), response.getStatus());
      setCompetitorStatus(status, response.getStatus());
    } else {
      log.error("Response is null!");
      setCompetitorStatus(CompetitorStatus.READ_ERROR);
    }
  }

  private void createDoc() {
    int httpStatus = openDocument();
    competitor.setHttpStatus(httpStatus);

    if (httpStatus < 200) {
      competitor.setStatus(CompetitorStatus.SOCKET_ERROR);
    } else if (httpStatus >= 400 && httpStatus != 503) {
      competitor.setStatus(CompetitorStatus.NETWORK_ERROR);
    } else if (httpStatus == 503) {
      competitor.setStatus(CompetitorStatus.BLOCKED);
    } else if (httpStatus != 200) {
      log.warn("Http status: {} for url: {}", httpStatus, getUrl());
    }
  }

  protected int openDocument() {
    String url = getAlternativeUrl();
    if (url == null) {
      url = getUrl();
    }
    try {
      Connection.Response 
      response = 
        Jsoup
          .connect(url)
          .headers(Global.standardHeaders)
          .userAgent(UserAgents.findARandomUA())
          .referrer(UserAgents.findARandomReferer())
          .timeout(5 * 1000)
          .followRedirects(true)
        .execute();
      response.charset("UTF-8");
      doc = response.parse();
      return response.statusCode();
    } catch (HttpStatusException httpe) {
      log.error("HttpStatusException for " + url);
      log.error(httpe.getMessage());
      competitor.setStatus(CompetitorStatus.NETWORK_ERROR);
      return httpe.getStatusCode();
    } catch (IOException e) {
      log.error("IOException for " + url);
      log.error(e.getMessage());
      competitor.setStatus(CompetitorStatus.SOCKET_ERROR);
      return 0;
    }
  }

}
