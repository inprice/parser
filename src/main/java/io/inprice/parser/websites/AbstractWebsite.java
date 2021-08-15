package io.inprice.parser.websites;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriver.Capability;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
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
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.HttpStatus;

/**
 * 
 * @author mdpinar
 */
public abstract class AbstractWebsite implements Website {

	private static final Logger logger = LoggerFactory.getLogger(AbstractWebsite.class);
	
	/**
	 * There are two kind of html handler;
	 *   a) External browser  (default one)
	 *   b) Internal HtmlUnit
	 */
	protected static enum Renderer {
		HTMLUNIT,
		BROWSER,
		JSOUP;  //for only tests
	}

	private Link link;
	private LinkStatus oldStatus;
	
	@Override
	public void check(Link link) {
		this.link = link;
		this.oldStatus = link.getStatus();

		if (openPage()) read();
	}

	protected Renderer getRenderer() {
		return Renderer.BROWSER;
	}

	private boolean openPage() {
		long started = System.currentTimeMillis();
		
		HttpStatus status = new HttpStatus(200, null);
		
		switch (getRenderer()) {
			case BROWSER: {
      	ProfilesIni profileIni = new ProfilesIni();
      	FirefoxProfile profile = profileIni.getProfile("default");
    
    		FirefoxOptions capabilities = new FirefoxOptions();
    		capabilities.setCapability(Capability.PROFILE, profile);
    		capabilities.setAcceptInsecureCerts(false);
    
    		FirefoxDriver webDriver = new FirefoxDriver(capabilities);
    		try {
      		webDriver.get(getUrl());

      		//some sites open a panel first to specify user preferences
      		if (clickFirstBy() != null) {
      			webDriver.findElement(clickFirstBy()).click();
      		}

      		//some sites loads all the data after sometime, so we need to wait some extra seconds!
      		if (waitBy() != null) {
        		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        		wait.until(ExpectedConditions.visibilityOfElementLocated(waitBy()));
      		}
    
          status = setHtml(webDriver.getPageSource());
          if (status.getMessage() == null) {
          	String url = getExtraUrl();
        		if (StringUtils.isNotBlank(url)) {
        			webDriver.get(url);
        			status = setExtraHtml(webDriver.getPageSource());
        		}
          }
    
    		} catch (Exception e) {
    			status.setMessage(e.getMessage());
    		} finally {
    			webDriver.close();
    		}
    		break;
			}
			
			case HTMLUNIT: {
    		WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    		try {
    			WebRequest req = new WebRequest(new URL(getUrl()));
    			req.setAdditionalHeader(HttpHeader.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    			req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
    			req.setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, "gzip, deflate, br");
    			req.setAdditionalHeader(HttpHeader.CONNECTION, "keep-alive");
    			req.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
    
    	  	WebResponse res = webClient.loadWebResponse(req);
    
    			if (res.getStatusCode() < 400) {
    				status.setCode(res.getStatusCode());
      			setHtml(res.getContentAsString());
    				afterRequest(webClient);
    			} else {
    				status.setCode(res.getStatusCode());
    				status.setMessage(res.getStatusMessage());
    			}
    
        } catch (SocketTimeoutException set) {
    			status.setCode(408);
    			status.setMessage("TIMED OUT!" + (link.getRetry() < 3 ? " RETRYING..." : ""));
    			logger.error("Timed out: {}", set.getMessage());
    		} catch (IOException e) {
    			logger.error("Unexpected error!", e);
    			status.setCode(502);
    			status.setMessage(e.getMessage());
    		} finally {
    			webClient.close();
    		}
    		break;
			}

			case JSOUP: {
				Connection.Response res = null;
		    try {
		      res = Jsoup.connect(getUrl()).userAgent("Mozilla").ignoreHttpErrors(true).execute();
		      if (res.statusCode() < 400) {
  		      res.charset("UTF-8");
  		      Document doc = res.parse();
  		      setHtml(doc.html());
		      } else {
    				status.setCode(res.statusCode());
    				status.setMessage(res.statusMessage());
		      }
		    } catch (IOException e) {
		    	if (res != null && res.statusCode() >= 400) {
    				status.setCode(res.statusCode());
    				status.setMessage(res.statusMessage());
		    	} else {
	    			status.setCode(502);
	    			status.setMessage(e.getMessage());
		    	}
		    }
				break;
			}
		
		}
		
		String logPart = String.format("Platform: %s, Status: %d, Time: %dms", link.getPlatform().getDomain(), status.getCode(), (System.currentTimeMillis() - started) / 10);

		if (status.getMessage() != null) {
			status.setMessage(io.inprice.common.utils.StringUtils.clearErrorMessage(status.getMessage()));
			if (status.getMessage().toLowerCase().contains("time out") || status.getMessage().toLowerCase().contains("timed out")) {
				setLinkStatus(LinkStatus.TIMED_OUT, status.getMessage(), (status.getCode() != 200 ? status.getCode() : 408));
			} else {
				LinkStatus linkStatus = LinkStatus.NETWORK_ERROR;
				switch (status.getCode()) {
  				case 403:
  					linkStatus = LinkStatus.NOT_ALLOWED;
  					break;
					case 404:
						linkStatus = LinkStatus.NOT_FOUND;
						break;
					case 503:
						linkStatus = LinkStatus.SITE_DOWN;
						break;
				}
				setLinkStatus(linkStatus, status.getMessage(), status.getCode());
			}
			logger.warn("---FAILED--- {}, Problem: {}, URL: {}", logPart, status.getMessage(), getUrl());
		} else {
			link.setHttpStatus(status.getCode());
			logger.info("-SUCCESSFUL- {}, URL: {}", logPart, getUrl());
		}

		return (status.getMessage() == null);
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
			link.setStatus(LinkStatus.NO_DATA);
			link.setProblem("HAS NO PRICE OR NAME");
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

	/**
	 * Comes from db
	 */
	@Override
	public String getUrl() {
		return link.getUrl();
	}

	/**
	 * Used for making an extra call to the website
	 * @return
	 */
	protected String getExtraUrl() { return null; }

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

	protected Set<LinkSpec> getFlatKeyValueSpecs(Elements keysSelector, Elements valsSelector) {
		Set<LinkSpec> specs = null;
		if (keysSelector != null && keysSelector.size() > 0) {
			specs = new HashSet<>();
			
			for (int i = 0; i < keysSelector.size(); i++) {
				Element key = keysSelector.get(i);
				Element value = (i < valsSelector.size() ? valsSelector.get(i) : null);
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

		String newForm = StringEscapeUtils.unescapeHtml4(io.inprice.common.utils.StringUtils.clearEmojies(val));
		if (StringUtils.isNotBlank(newForm) && newForm.length() > limit)
			return SqlHelper.clear(newForm.substring(0, limit));
		else
			return SqlHelper.clear(newForm);
	}

	protected HttpStatus setHtml(String html) {
		return new HttpStatus(200, null);
	}

	protected HttpStatus setExtraHtml(String html) {
		return new HttpStatus(200, null);
	}

	protected By clickFirstBy() {
		return null;
	}

	protected By waitBy() {
		return null;
	}
	
	protected void afterRequest(WebClient webClient) { }
	
}
