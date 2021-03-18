package io.inprice.parser.websites;

import static io.inprice.parser.helpers.Global.HTMLUNIT_POOL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.inprice.common.config.SysProps;
import io.inprice.common.helpers.SqlHelper;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.models.Platform;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.config.Props;
import io.inprice.parser.helpers.Consts;
import kong.unirest.HttpResponse;

/**
 * 
 * @author mdpinar
 */
public abstract class AbstractWebsite implements Website {

	private static final Logger log = LoggerFactory.getLogger(AbstractWebsite.class);
	
	/**
	 * There are two kind of html handler; a) External chrome browser, b) Internal HtmlUnit (default one)
	 */
	protected static enum Renderer {
		HTMLUNIT, //internal and default one
		CHROME;  //external and optional
	}

	private Link link;
	private LinkStatus oldStatus;
	
	private String html;

	@Override
	public void check(Link link) {
		this.link = link;
		this.oldStatus = link.getStatus();

		if (openPage()) read();
	}
	
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	private boolean openPage() {
		String url = getAlternativeUrl();
		if (StringUtils.isBlank(url)) url = getUrl();

		long started = System.currentTimeMillis();

		String problem = null;
		int httpStatus = 200;

		if (Renderer.HTMLUNIT.equals(getRenderer())) {
			WebClient webClient = null;
			try {
	    	WebRequest req = new WebRequest(new URL(url));
	    	beforeRequest(req);

				webClient = HTMLUNIT_POOL.acquire();

				HtmlPage page = webClient.getPage(req);
				if (page.getWebResponse().getStatusCode() < 400) {
					httpStatus = page.getWebResponse().getStatusCode();
	  			setHtml(page.getTextContent());
	  			afterRequest(webClient);
				} else {
					problem = page.getWebResponse().getStatusMessage();
					httpStatus = page.getWebResponse().getStatusCode();
				}

	    } catch (SocketTimeoutException set) {
				problem = "TIMED OUT!" + (link.getRetry() < 3 ? " RETRYING..." : "");
				httpStatus = 408;
				log.error("Timed out: {}", set.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				problem = e.getMessage();
				httpStatus = 502;
			} finally {
				if (webClient != null) HTMLUNIT_POOL.release(webClient);
			}
		}

		if (Renderer.CHROME.equals(getRenderer())) {
  		RemoteWebDriver webDriver = null;
  		try {
    		ChromeOptions options = new ChromeOptions();
      	options.addArguments(
      		"--disable-gpu",
      		"--disable-dev-shm-usage",
      		"--no-sandbox",
      		"--ignore-certificate-errors",
    			"--disable-blink-features=AutomationControlled"
  			);
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        options.setCapability("goog:loggingPrefs", logPrefs);
      	
        webDriver = new RemoteWebDriver(new URL(Props.WEBDRIVER_URL()), options);
      	webDriver.manage().timeouts().pageLoadTimeout(SysProps.HTTP_CONNECTION_TIMEOUT(), TimeUnit.SECONDS);
    		webDriver.get(url);
    		
  			LogEntries logs = webDriver.manage().logs().get(LogType.PERFORMANCE);
  			for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
          LogEntry entry = it.next();
          try {
            JSONObject json = new JSONObject(entry.getMessage());
            JSONObject message = json.getJSONObject("message");
            String method = message.getString("method");
            if (method != null && "Network.responseReceived".equals(method)) {
              JSONObject params = message.getJSONObject("params");
              JSONObject response = params.getJSONObject("response");
              String messageUrl = response.getString("url");
              if (url.equals(messageUrl)) {
              	httpStatus = response.getInt("status");
                break;
              }
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }  			
    		
  			if (httpStatus >= 400) {
  				problem = EnglishReasonPhraseCatalog.INSTANCE.getReason(httpStatus, Locale.ENGLISH);
  			} else {
  				String pageSource = null;
  				if (link.getPlatform().isJsRenderedBody()) {
  	  			String javascript = "return arguments[0].innerHTML";
  	  			pageSource = (String)((JavascriptExecutor)webDriver).executeScript(javascript, webDriver.findElement(By.tagName("html")));
  				} else {
  					pageSource = webDriver.getPageSource();
  				}
  				setHtml(pageSource);
  			}
  			//some websites like canadiantire needs extra http call for some data such as price and stock availability.
  			//renderExtra(webDriver);
  
  			webDriver.close();
  		} catch (TimeoutException e) {
  			//TODO: burasi cok iyi olmadi?!?! sayfada spesific bir element aranip yoksa timeout oldu diye isaretlenmeli!!!
  			WebElement html = webDriver.findElement(By.tagName("html"));
  			if (html.isDisplayed()) {
  				setHtml(html.getText());
  			} else {
  				problem = "TIMED OUT!" + (link.getRetry() < 3 ? " RETRYING..." : "");
    			httpStatus = 408;
    			log.error("Timed out: {}", e.getMessage());
  			}
  		} catch (WebDriverException e) {
  			problem = "ACCESS ERROR!";
  			httpStatus = 407;
  			log.error("Reaching error: {}", e.getMessage());
  		} catch (MalformedURLException e) {
  			e.printStackTrace();
  			problem = "MALFORMED URL!";
  			httpStatus = 500;
			} finally {
  			if (webDriver != null) webDriver.quit();
  		}
		}

		String logPart = String.format("Platform: %s, Status: %d, Time: %dms", link.getPlatform().getDomain(), httpStatus, (System.currentTimeMillis() - started) / 10);

		if (problem != null) {
			problem = io.inprice.common.utils.StringUtils.clearErrorMessage(problem);
			if (problem.toLowerCase().contains("time out") || problem.toLowerCase().contains("timed out")) {
				setLinkStatus(LinkStatus.TIMED_OUT, problem, (httpStatus != 200 ? httpStatus : 408));
			} else {
				setLinkStatus((httpStatus == 404 ? LinkStatus.NOT_FOUND : LinkStatus.NETWORK_ERROR), problem, (httpStatus != 200 ? httpStatus : 206));
			}
			log.warn("---FAILED--- {}, Problem: {}, URL: {}", logPart, problem, url);
		} else {
			link.setHttpStatus(httpStatus);
			log.info("-SUCCESSFUL- {}", logPart);
		}

		return (problem == null);
	}

	private void read() {
		//setHtml method may set a different group. thus, we need to check if it is so
		if (!LinkStatus.ACTIVE_GROUP.equals(link.getStatus().getGroup())) {
			return;
		}

		// base settings
		String name = getName();
		BigDecimal price = getPrice();

		if (price == null || name == null || price.compareTo(BigDecimal.ONE) < 0 || Consts.Words.NOT_AVAILABLE.equals(name)) {
			analyseTheProblem();
			return;
		}

		// other settings
		link.setSku(fixLength(getSku(), Consts.Limits.SKU));
		link.setName(fixLength(name, Consts.Limits.NAME));
		link.setPrice(price.setScale(2, RoundingMode.HALF_UP));

		// if it is not a product import
		if (link.getImportDetailId() == null) {
  		link.setBrand(fixLength(getBrand(), Consts.Limits.BRAND));
  		link.setSeller(fixLength(getSeller(), Consts.Limits.SELLER));
  		link.setShipment(fixLength(getShipment(), Consts.Limits.SHIPMENT));

  		// spec list editing
  		List<LinkSpec> specList = getSpecList();
  		if (specList != null && specList.size() > 0) {
  			List<LinkSpec> newList = new ArrayList<>(specList.size());
  			for (LinkSpec ls : specList) {
  				newList.add(new LinkSpec(fixLength(ls.getKey(), Consts.Limits.SPEC_KEY), fixLength(ls.getValue(), Consts.Limits.SPEC_VALUE)));
  			}
  			link.setSpecList(newList);
  		}
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

	@Override
	public String getUrl() {
		return link.getUrl();
	}
	
	protected int getRetry() {
		return link.getRetry();
	}
	
	protected Platform getPlatform() {
		return link.getPlatform();
	}

	protected List<LinkSpec> getValueOnlySpecList(Elements specs) {
		return getValueOnlySpecList(specs, null);
	}

	protected List<LinkSpec> getValueOnlySpecList(Elements specs, String sep) {
		List<LinkSpec> specList = null;
		if (specs != null && specs.size() > 0) {
			specList = new ArrayList<>();
			for (Element spec : specs) {
				LinkSpec ls = new LinkSpec("", spec.text());
				if (StringUtils.isNotBlank(spec.text())) {
					if (sep != null && ls.getValue().indexOf(sep) > 0) {
						String[] specChunks = ls.getValue().split(sep);
						ls.setKey(specChunks[0]);
						ls.setValue(specChunks[1]);
					}
				}
				specList.add(ls);
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
		return findAPart(html, starting, ending, plus, 0);
	}
	
	protected String findAPart(String html, String starting, String ending, int plus, int startPointOffset) {
		int start = html.indexOf(starting) + (startPointOffset <= 0 ? starting.length() : 0);
		int end = html.indexOf(ending, start) + plus;

		if (start > starting.length() && end > start) {
			return html.substring(start + startPointOffset, end);
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

	protected void setLinkStatus(LinkStatus status, String problem, int httpStatus) {
		link.setStatus(status);
		link.setProblem(problem.toUpperCase());
		link.setHttpStatus(httpStatus);
	}

	private String fixLength(String val, int limit) {
		if (val == null) return null;

		String newForm = io.inprice.common.utils.StringUtils.clearEmojies(val);
		if (StringUtils.isNotBlank(newForm) && newForm.length() > limit)
			return SqlHelper.clear(newForm.substring(0, limit));
		else
			return SqlHelper.clear(newForm);
	}

	private void analyseTheProblem() {
		detectProblem();
		
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("user.home"));
		sb.append("/tmp/");
		sb.append(link.getPlatform().getName().replaceAll("\\s",  ""));
		sb.append("-");
		sb.append(link.getId());
		sb.append(".html");

		log.warn(" - Status: {}, Pre.Status: {}, File: {}", link.getStatus().name(), oldStatus.name(), sb.toString());
		
		try (PrintWriter out = new PrintWriter(sb.toString())) {
	    out.println(this.html);
		} catch (FileNotFoundException e) {
			log.error("Failed to journal the problem!", e);
		}		
	}
	
	protected void setHtml(String html) {
		this.html = html;
	}
	
	protected String getHtml() {
		return this.html;
	}
	
	protected void detectProblem() {
		if (LinkStatus.AVAILABLE.equals(oldStatus)) {
			setLinkStatus(LinkStatus.NOT_AVAILABLE, "AVAILABILITY PROBLEM");
		} else {
			setLinkStatus(LinkStatus.NO_DATA, "HAS NO PRICE OR NAME");
		}
		link.setHttpStatus(404);
	}
	
	protected void beforeRequest(WebRequest req) { }
	protected void afterRequest(WebClient webClient) { }

	protected String getAlternativeUrl() { return null; }

}
