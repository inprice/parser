package io.inprice.parser.pool;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Describes a pool for webclients
 * 
 * @author mdpinar
 * @since 2021-03-13
 *
 */
public class HtmlUnitPool extends ResourcePool<WebClient> {

	//TODO: eski haline dondurulmeli
	
	public HtmlUnitPool() {
		//super("WebClient", Props.ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY());
		super("HtmlUnit", 1);
	}

	@Override
	public WebClient createNewOne() {
		//WebClient webClient = new WebClient(BrowserVersion.FIREFOX, Props.PROXY_HOST(), Props.PROXY_PORT());
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

    //proxy settings
    /*
    DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
    scp.addCredentials(Props.PROXY_USERNAME(), Props.PROXY_PASSWORD(), Props.PROXY_HOST(), Props.PROXY_PORT(), null);
    webClient.setCredentialsProvider(scp);
    */

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
