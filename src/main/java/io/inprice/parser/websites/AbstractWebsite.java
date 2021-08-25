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

import org.apache.commons.collections4.CollectionUtils;
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
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
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
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.ParseCode;
import io.inprice.parser.info.ParseStatus;

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
		JSOUP;  //just for tests
	}

	@Override
	public ParseStatus check(Link link) {
		LinkStatus oldStatus = link.getStatus();

		ParseStatus status = openPage(link);
		if (status.getCode().equals(ParseCode.OK)) {
			status = read(link, oldStatus);
		}
		return status;
	}

	private ParseStatus openPage(Link link) {
		ParseStatus status = OK_Status();

		switch (getRenderer()) {
			case BROWSER: {
      	ProfilesIni profileIni = new ProfilesIni();
      	FirefoxProfile profile = profileIni.getProfile("default");
    
    		FirefoxOptions capabilities = new FirefoxOptions();
    		capabilities.setLogLevel(FirefoxDriverLogLevel.FATAL);
    		capabilities.setCapability(Capability.PROFILE, profile);
    		capabilities.setAcceptInsecureCerts(false);

    		FirefoxDriver webDriver = new FirefoxDriver(capabilities);
    		try {
      		webDriver.get(link.getUrl());

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
          	String url = getExtraUrl(link.getUrl());
        		if (StringUtils.isNotBlank(url)) {
        			webDriver.get(url);
        			status = setExtraHtml(webDriver.getPageSource());
        		}
          }
    
    		} catch (Exception e) {
    			status = new ParseStatus(ParseCode.OTHER_EXCEPTION, e.getMessage());
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
    			WebRequest req = new WebRequest(new URL(link.getUrl()));
    			req.setAdditionalHeader(HttpHeader.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    			req.setAdditionalHeader(HttpHeader.ACCEPT_LANGUAGE, "en-US,en;q=0.5");
    			req.setAdditionalHeader(HttpHeader.ACCEPT_ENCODING, "gzip, deflate, br");
    			req.setAdditionalHeader(HttpHeader.CONNECTION, "keep-alive");
    			req.setAdditionalHeader(HttpHeader.CACHE_CONTROL, "max-age=0");
    
    	  	WebResponse res = webClient.loadWebResponse(req);
    
    			if (res.getStatusCode() < 400) {
      			status = setHtml(res.getContentAsString());
      			if (status.getCode().equals(ParseCode.OK)) {
      				status = afterRequest(webClient);
      			}
    			} else {
    				status = new ParseStatus(ParseCode.HTTP_OTHER_ERROR, res.getStatusCode() + ": " + res.getStatusMessage());
    			}
    
        } catch (SocketTimeoutException set) {
        	status = new ParseStatus(ParseCode.TIMEOUT_EXCEPTION, set.getMessage());
    			logger.error("Timed out: {}", set.getMessage());
    		} catch (IOException e) {
    			status = new ParseStatus(ParseCode.IO_EXCEPTION, e.getMessage());
    			logger.error("Unexpected error!", e);
    		} finally {
    			webClient.close();
    		}
    		break;
			}

			case JSOUP: {
				Connection.Response res = null;
		    try {
		      res = Jsoup.connect(link.getUrl()).userAgent("Mozilla").ignoreHttpErrors(true).execute();
		      if (res.statusCode() < 400) {
  		      Document doc = res.parse();
  		      status = setHtml(doc.html());
		      } else {
		      	status = new ParseStatus(ParseCode.HTTP_OTHER_ERROR, res.statusCode() + ": " + res.statusMessage());
		      }
		    } catch (IOException e) {
		    	if (res != null && res.statusCode() >= 400) {
	    			status = new ParseStatus(ParseCode.HTTP_OTHER_ERROR, res.statusCode() + ": " + res.statusMessage());
		    	} else {
		    		status = new ParseStatus(ParseCode.IO_EXCEPTION, e.getMessage());
		    	}
		    }
				break;
			}
		
		}

		return status;
	}

	private ParseStatus read(Link link, LinkStatus oldStatus) {
		String name = getName();
		BigDecimal price = getPrice();

		if (StringUtils.isBlank(name) || Consts.Words.NOT_AVAILABLE.equals(name) || price == null || price.compareTo(BigDecimal.ONE) < 0) {
			return new ParseStatus(ParseCode.NO_DATA, "No data");
		}

		// other settings
		link.setSku(fixLength(getSku(link.getUrl()), Consts.Limits.SKU));
		link.setName(fixLength(name, Consts.Limits.NAME));
		link.setPrice(price.setScale(2, RoundingMode.HALF_UP));

		link.setBrand(fixLength(getBrand(), Consts.Limits.BRAND));
		link.setSeller(fixLength(getSeller(link.getPlatform().getName()), Consts.Limits.SELLER));
		link.setShipment(fixLength(getShipment(), Consts.Limits.SHIPMENT));

		// spec list editing
		Set<LinkSpec> specs = getSpecs();
		if (CollectionUtils.isNotEmpty(specs)) {
			List<LinkSpec> newList = new ArrayList<>(specs.size());
			for (LinkSpec ls : specs) {
				newList.add(new LinkSpec(fixLength(ls.getKey(), Consts.Limits.SPEC_KEY), fixLength(ls.getValue(), Consts.Limits.SPEC_VALUE)));
			}
			link.setSpecList(newList);
		}

		if (isAvailable()) {
			return OK_Status();
		} else {
			return new ParseStatus(ParseCode.NOT_AVAILABLE, "Insufficient stock");
		}
	}

	protected Renderer getRenderer() {
		return Renderer.BROWSER;
	}

	@Override
	public String getSeller(String defaultSeller) {
		return defaultSeller;
	}
	
	/**
	 * Used for making an extra call to the website
	 * @return
	 */
	protected String getExtraUrl(String url) { return null; }

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

	private String fixLength(String val, int limit) {
		if (val == null) return null;

		String newForm = StringEscapeUtils.unescapeHtml4(io.inprice.common.utils.StringUtils.clearEmojies(val));
		if (StringUtils.isNotBlank(newForm) && newForm.length() > limit)
			return SqlHelper.clear(newForm.substring(0, limit));
		else
			return SqlHelper.clear(newForm);
	}

	protected ParseStatus setHtml(String html) {
		return OK_Status();
	}

	protected ParseStatus setExtraHtml(String html) {
		return OK_Status();
	}

	protected ParseStatus afterRequest(WebClient webClient) {
		return OK_Status();
	}

	protected By clickFirstBy() {
		return null;
	}

	protected By waitBy() {
		return null;
	}

	protected Set<LinkSpec> getValueOnlySpecs(Elements specs) {
		return getValueOnlySpecs(specs, null);
	}

	protected Set<LinkSpec> getValueOnlySpecs(Elements specs, String sep) {
		Set<LinkSpec> specList = null;
		if (CollectionUtils.isNotEmpty(specs)) {
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
		if (CollectionUtils.isNotEmpty(specsEl)) {
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
		if (CollectionUtils.isNotEmpty(keysSelector)) {
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

	protected ParseStatus OK_Status() {
		return new ParseStatus(ParseCode.OK, null);
	}

}
