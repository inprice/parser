package io.inprice.parser.websites;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vdurmont.emoji.EmojiParser;

import io.inprice.common.config.SysProps;
import io.inprice.common.helpers.Beans;
import io.inprice.common.helpers.SqlHelper;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.config.Props;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.Global;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.helpers.UserAgents;
import kong.unirest.HttpResponse;

public abstract class AbstractWebsite implements Website {

	protected static final Logger log = LoggerFactory.getLogger(AbstractWebsite.class);

	protected HttpClient httpClient = Beans.getSingleton(HttpClient.class);

	private Link link;
	private LinkStatus oldStatus;

	protected Document doc;
	protected JSONObject json;

	@Override
	public Link check(Link link) {
		this.link = SerializationUtils.clone(link);
		this.oldStatus = this.link.getStatus();

		if (willHtmlBePulled()) {
			if (openPage())
				read();
		} else {
			read();
		}
		return this.link;
	}

	public boolean willHtmlBePulled() {
		return true;
	}

	@Override
	public String getUrl() {
		if (link == null)
			link = getTestLink();
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

	protected LinkStatus getLinkStatus() {
		return link.getStatus();
	}

	protected void setLinkStatus(HttpResponse<String> response) {
		String problem = "Empty response!";
		if (response != null) {
			problem = response.getStatusText();
			link.setHttpStatus(response.getStatus());
		}
		setLinkStatus(LinkStatus.NETWORK_ERROR, problem);
	}

	protected void setLinkStatus(LinkStatus status, String problem) {
		link.setStatus(status);
		link.setProblem(problem.toUpperCase());
	}

	protected boolean openPage() {
		String url = getAlternativeUrl();
		if (url == null) {
			url = getUrl();
		}

		long started = System.currentTimeMillis();

		String problem = null;
		int httpStatus = 200;
		
		boolean viaBrowser = false;
		int randomPossibilty = 0;

		if (link.getPlatform().getBrowserPossibility() >= 100) {
			randomPossibilty = 100;
		} else if (link.getPlatform().getBrowserPossibility() > 0) {
			randomPossibilty = (int)(Math.random() * 100)+1;
		}

		//via chrome browser
		if (randomPossibilty > 0 && randomPossibilty <= link.getPlatform().getBrowserPossibility()) {
			viaBrowser = true;
			WebDriver webDriver = null;
  		try {
    		ChromeOptions options = new ChromeOptions();
      	options.addArguments(
    			"--start-maximized",
      		"--headless", 
      		"--disable-gpu",
      		"--disable-dev-shm-usage",
      		"--no-sandbox",
      		"--blink-settings=imagesEnabled=false",
      		"--ignore-certificate-errors",
      		"--user-agent=" + UserAgents.getRandomUserAgent()
  			);
      	
      	webDriver = new RemoteWebDriver(new URL(Props.WEBDRIVER_URL()), options);
      	webDriver.manage().timeouts().pageLoadTimeout(SysProps.HTTP_CONNECTION_TIMEOUT() + link.getPlatform().getExtraTimeout(), TimeUnit.SECONDS);
      	if (link.getPlatform().isLoadingMainPageFirst()) {
      		webDriver.get("https://www." + link.getPlatform().getDomain());
      		//some websites need to a resting time before a new request.
      		//here, we are giving a twenty percent chance to wait one second more!
      		int waitPossibility = (int)(Math.random() * 100)+1; //(1..100)
      		if (waitPossibility <= 20) {
      			try {
							Thread.sleep(1000);
						} catch (InterruptedException e) { }
      		}
      	}
  			webDriver.get(url);
  			doc = Jsoup.parse(webDriver.getPageSource());
  			webDriver.close();
  		} catch (TimeoutException e) {
  			problem = "TIMED OUT! RETRYING...";
  			httpStatus = 408;
  			log.error("Failed to load page: {}", e.getMessage());
  		} catch (MalformedURLException e) {
  			problem = "INCOMPATIBLE URL!";
  			httpStatus = 400;
  			log.error("Failed to load page: {}", e.getMessage());
  		} finally {
  			if (webDriver != null) webDriver.quit();
  		}

		} else { //via jsoup
  		try {
        Connection.Response 
        response = Jsoup.connect(url)
          .headers(Global.standardHeaders)
          .userAgent(UserAgents.getRandomUserAgent())
          .proxy(Props.PROXY_HOST(), Props.PROXY_PORT())
          .timeout(SysProps.HTTP_CONNECTION_TIMEOUT() * 1000)
        .execute();
        response.charset("UTF-8");
    
        doc = response.parse();
      } catch (HttpStatusException hse) {
        problem = hse.getMessage();
        httpStatus = hse.getStatusCode();
  		} catch (Exception e) {
  			problem = e.getMessage();
  			httpStatus = 502;
  		}
		}

		//instant express for logging
		String logPart = String.format(
			"Time: %dms, [%s via %s], Expected/Random: (%d/%d)",
			(System.currentTimeMillis() - started) / 10, 
			link.getPlatform().getName(), 
			(viaBrowser ? "Chrome" : "JSoup"), 
			link.getPlatform().getBrowserPossibility(),
			randomPossibilty
		);

		if (problem != null) {
			problem = io.inprice.common.utils.StringUtils.clearErrorMessage(problem);
			if (problem.matches("(?i)time.*out")) {
				setLinkStatus(LinkStatus.TIMED_OUT, problem);
			} else {
				setLinkStatus(LinkStatus.NETWORK_ERROR, problem);
			}
			link.setHttpStatus(httpStatus);
			log.warn("----FAILED---- {}, Problem: {}, URL: {}", logPart, problem, url);
		} else {
			log.info("--SUCCESSFUL-- {}", logPart);
		}

		return (problem == null);
	}

	private String fixLength(String val, int limit) {
		if (val == null) return null;

		String newForm = EmojiParser.removeAllEmojis(io.inprice.common.utils.StringUtils.fixQuotes(val)).trim();
		if (StringUtils.isNotBlank(newForm) && newForm.length() > limit)
			return SqlHelper.clear(newForm.substring(0, limit));
		else
			return SqlHelper.clear(newForm);
	}

	private void read() {
		json = getJsonData();

		// getJsonData method may return an error. thus, we need to check if it is so
		if (!LinkStatus.ACTIVE_GROUP.equals(link.getStatus().getGroup())) {
			return;
		}

		// price settings
		BigDecimal price = getPrice();
		if (price == null || getName() == null || price.compareTo(BigDecimal.ONE) < 0 || Consts.Words.NOT_AVAILABLE.equals(getName())) {
			if (LinkStatus.AVAILABLE.equals(oldStatus)) {
				setLinkStatus(LinkStatus.NOT_AVAILABLE, "AVAILABILITY PROBLEM");
			} else {
				setLinkStatus(LinkStatus.NO_DATA, "HAS NO PRICE OR NAME");
			}
			link.setHttpStatus(404);

			log.warn("URL: " + getUrl());
			log.warn(" - Status: {}, Pre.Status: {}", link.getStatus().name(), oldStatus.name());
			return;
		}

		// other settings
		link.setSku(fixLength(getSku(), Consts.Limits.SKU));
		link.setName(fixLength(getName(), Consts.Limits.NAME));
		link.setPrice(price);
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
			if (link.getImportDetailId() == null || StringUtils.isBlank(link.getName())) {
				setLinkStatus(LinkStatus.NOT_AVAILABLE, "INSUFFICIENT STOCK");
			} else {
				link.setStatus(LinkStatus.AVAILABLE);
			}
		}

	}

}
