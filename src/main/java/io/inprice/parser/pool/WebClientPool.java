package io.inprice.parser.pool;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;

import io.inprice.parser.config.Props;

/**
 * Describes a pool for webclients
 * 
 * @author mdpinar
 * @since 2021-03-13
 *
 */
public class WebClientPool extends ResourcePool<WebClient> {

	public WebClientPool() {
		super("WebClient", Props.ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY());
	}

	@Override
	public WebClient createNewOne() {
		//do you hate internet explorer like me!
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER, Props.PROXY_HOST(), Props.PROXY_PORT());
		webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

    //proxy settings
    DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
    scp.addCredentials(Props.PROXY_USERNAME(), Props.PROXY_PASSWORD(), Props.PROXY_HOST(), Props.PROXY_PORT(), null);
    webClient.setCredentialsProvider(scp);

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
