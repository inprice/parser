package io.inprice.parser.websites;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import io.inprice.common.helpers.SqlHelper;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.meta.LinkStatusGroup;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.models.Platform;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.config.Props;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.Global;

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
		HEADLESS,  //external and optional
		JSOUP;  //for test
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
		long started = System.currentTimeMillis();

		String problem = null;
		int httpStatus = 200;
		
		switch (getRenderer()) {

			case HTMLUNIT: {
				WebClient webClient = new WebClient(BrowserVersion.FIREFOX, Props.PROXY_HOST, Props.PROXY_PORT);
  			webClient.getOptions().setThrowExceptionOnScriptError(false);
  	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
  	    webClient.getOptions().setRedirectEnabled(true);

  	    //WARN: attention pls!!!
  	    //webClient.getOptions().setDownloadImages(false);

  	    if (StringUtils.isNotBlank(Props.PROXY_HOST)) {
  	      DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
  	      scp.addCredentials(Props.PROXY_USERNAME, Props.PROXY_PASSWORD);
  	      webClient.setCredentialsProvider(scp);
  	    }

  			try {
  				WebResponse res = makeRequest(webClient);

  				if (res.getStatusCode() < 400) {
  					httpStatus = res.getStatusCode();
  	  			setHtml(res.getContentAsString());
  	  			afterRequest(webClient);
  				} else {
  					problem = res.getStatusMessage();
  					httpStatus = res.getStatusCode();
  				}

  	    } catch (SocketTimeoutException set) {
  				problem = "TIMED OUT!" + (link.getRetry() < 3 ? " RETRYING..." : "");
  				httpStatus = 408;
  				log.error("Timed out: {}", set.getMessage());
  			} catch (IOException e) {
  				log.error("Unexpected error!", e);
  				problem = e.getMessage();
  				httpStatus = 502;
  			} finally {
  				if (webClient != null) webClient.close();
  			}
  			break;
  		}

			case HEADLESS: {
				WebDriver webDriver = Global.getWebDriver();
    		webDriver.get(getUrl());
				setHtml(webDriver.getPageSource());
  			break;
  		}

			case JSOUP: {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Props.PROXY_HOST, Props.PROXY_PORT));
				Connection.Response response = null;
		    try {
		    	response = Jsoup.connect(getUrl())
	    			.proxy(proxy)
	    			.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:90.0) Gecko/20100101 Firefox/90.0")
	    			.ignoreHttpErrors(true)
            .execute();
		      if (response.statusCode() < 400) {
  		      response.charset("UTF-8");
  		      Document doc = response.parse();
  		      setHtml(doc.html());
		      } else {
	  				problem = response.statusMessage();
	  				httpStatus = response.statusCode();
		      }
		    } catch (IOException e) {
		    	if (response != null && response.statusCode() >= 400) {
	  				problem = response.statusMessage();
	  				httpStatus = response.statusCode();
		    	} else {
    				problem = e.getMessage();
    				httpStatus = 502;
		    	}
		    }
				break;
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
			log.warn("---FAILED--- {}, Problem: {}, URL: {}", logPart, problem, getUrl());
		} else {
			link.setHttpStatus(httpStatus);
			log.info("-SUCCESSFUL- {}", logPart);
		}

		return (problem == null);
	}

	private void read() {
		//setHtml method may set a different group. thus, we need to check if it is so
		//check if its old and new status are different and new status is not active!
		if (!oldStatus.equals(link.getStatus()) && !oldStatus.equals(LinkStatus.TOBE_CLASSIFIED) && !LinkStatusGroup.ACTIVE.equals(link.getStatus().getGroup())) {
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

		link.setBrand(fixLength(getBrand(), Consts.Limits.BRAND));
		link.setSeller(fixLength(getSeller(), Consts.Limits.SELLER));
		link.setShipment(fixLength(getShipment(), Consts.Limits.SHIPMENT));

		// spec list editing
		Set<LinkSpec> specs = getSpecs();
		if (specs != null && specs.size() > 0) {
			List<LinkSpec> newList = new ArrayList<>(specs.size());
			for (LinkSpec ls : specs) {
				newList.add(new LinkSpec(fixLength(ls.getKey(), Consts.Limits.SPEC_KEY), fixLength(ls.getValue(), Consts.Limits.SPEC_VALUE)));
			}
			link.setSpecList(newList);
		}

		if (isAvailable()) {
			link.setStatus(LinkStatus.AVAILABLE);
		} else {
			setLinkStatus(LinkStatus.NOT_AVAILABLE, "INSUFFICIENT STOCK");
		}
	}
	
	protected WebResponse makeRequest(WebClient webClient) throws MalformedURLException, IOException {
		String url = getAlternativeUrl();
		if (StringUtils.isBlank(url)) url = getUrl();

		WebRequest req = new WebRequest(new URL(url));
		req.setAdditionalHeader(HttpHeader.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
		req.setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, "gzip, deflate, br");
		req.setAdditionalHeader(HttpHeader.CONNECTION, "keep-alive");
		req.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
		
  	beforeRequest(req);
		return webClient.loadWebResponse(req);
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

	protected Set<LinkSpec> getValueOnlySpecs(Elements specs) {
		return getValueOnlySpecs(specs, null);
	}

	protected Set<LinkSpec> getValueOnlySpecs(Elements specs, String sep) {
		Set<LinkSpec> specList = null;
		if (specs != null && specs.size() > 0) {
			specList = new HashSet<>();
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

	protected Set<LinkSpec> getKeyValueSpecs(Elements specsEl, String keySelector, String valueSelector) {
		Set<LinkSpec> specs = null;
		if (specsEl != null && specsEl.size() > 0) {
			specs = new HashSet<>();
			for (Element spec : specsEl) {
				Element key = spec.selectFirst(keySelector);
				Element value = spec.selectFirst(valueSelector);
				if (key != null || value != null) {
					specs.add(
				    new LinkSpec((key != null ? key.text().replaceAll(":", "") : ""), (value != null ? value.text() : ""))
			    );
				}
			}
		}
		return specs;
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
	
	protected void setLinkStatus(LinkStatus status, String problem) {
		link.setStatus(status);
		link.setProblem(problem.toUpperCase());
	}

	protected void setLinkStatus(LinkStatus status, String problem, int httpStatus) {
		link.setStatus(status);
		link.setProblem(problem.toUpperCase());
		link.setHttpStatus(httpStatus);
	}
	
	public String getSeller() {
		return getPlatform().getName();
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

	protected boolean isJsRendered() { return false; }
	protected boolean willHtmlBePulled() { return true; }
	
	protected String getAlternativeUrl() { return null; }

}
