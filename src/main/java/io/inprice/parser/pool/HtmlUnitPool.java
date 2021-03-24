package io.inprice.parser.pool;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;

import io.inprice.parser.config.Props;

/**
 * Object pool for html
 * 
 * @author mdpinar
 * @since 2021-03-13
 *
 */
public class HtmlUnitPool extends ResourcePool<WebClient> {

	public HtmlUnitPool() {
		super("HtmlUnit", Props.ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY());
	}

	@Override
	public WebClient createNewOne() {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX, Props.PROXY_HOST(), Props.PROXY_PORT());
		webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    
    //WARN: buna dikkat!!!
    //webClient.getOptions().setDownloadImages(false);

    if (StringUtils.isNotBlank(Props.PROXY_HOST())) {
      DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
      scp.addCredentials(Props.PROXY_USERNAME(), Props.PROXY_PASSWORD(), Props.PROXY_HOST(), Props.PROXY_PORT(), null);
      webClient.setCredentialsProvider(scp);
    }

		return webClient;
	}

	@Override
	public void setFree(WebClient resource) {
		resource.close();
	}

	@Override
	public boolean isHealthy(WebClient resource) {
		return resource.getCurrentWindow().isClosed() == false;
	}

}
