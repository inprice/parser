package io.inprice.parser.websites;

import static io.inprice.parser.helpers.Global.WEB_CLIENT_POOL;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import io.inprice.common.config.SysProps;
import io.inprice.common.helpers.SqlHelper;
import io.inprice.common.meta.AppEnv;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.common.utils.NumberUtils;
import io.inprice.parser.helpers.Consts;
import kong.unirest.HttpResponse;

/**
 * 
 * @author mdpinar
 */
public abstract class AbstractWebsite implements Website {

	private static final Logger log = LoggerFactory.getLogger(AbstractWebsite.class);

	private Link link;
	private LinkStatus oldStatus;

	protected abstract void setHtml(String html);
	protected abstract String getHtml();

	@Override
	public void check(Link link) {
		this.link = link;
		this.oldStatus = link.getStatus();

		if (openPage()) read();
	}

	private boolean openPage() {
		String url = getAlternativeUrl();
		if (StringUtils.isBlank(url)) url = getUrl();

		long started = System.currentTimeMillis();

		String problem = null;
		int httpStatus = -1;

		WebClient webClient = null;
		try {
    	WebRequest req = new WebRequest(new URL(url));
    	beforeRequest(req);

			webClient = WEB_CLIENT_POOL.acquire();

			WebResponse res = webClient.loadWebResponse(req);
			if (res.getStatusCode() < 400) {
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
			e.printStackTrace();
			problem = e.getMessage();
			httpStatus = 502;
		} finally {
			if (webClient != null) WEB_CLIENT_POOL.release(webClient);
		}

		String logPart = String.format("Platform: %s, Status: %d, Time: %dms", link.getPlatform().getDomain(), httpStatus, (System.currentTimeMillis() - started) / 10);

		if (problem != null) {
			problem = io.inprice.common.utils.StringUtils.clearErrorMessage(problem);
			if (problem.toLowerCase().contains("time out") || problem.toLowerCase().contains("timed out")) {
				setLinkStatus(LinkStatus.TIMED_OUT, problem);
			} else {
				setLinkStatus(LinkStatus.NETWORK_ERROR, problem);
			}
			link.setHttpStatus(httpStatus);
			log.warn("---FAILED--- {}, Problem: {}, URL: {}", logPart, problem, url);
		} else {
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
		if (AppEnv.DEV.equals(SysProps.APP_ENV()) || link.getImportDetailId() == null) {
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
	    out.println(getHtml());
		} catch (FileNotFoundException e) {
			log.error("Failed to journal the problem!", e);
		}		
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
