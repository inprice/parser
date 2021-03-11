package io.inprice.parser.helpers;

import java.util.Map.Entry;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;

import io.inprice.parser.config.Props;

public class HtmlUnitManager {

	private static WebClient webClient;

	/**
	 * The only requester of this method is getClient() method!
	 * Meaning that you should not call directly!
	 */
	private static void start() {
		webClient = new WebClient(BrowserVersion.FIREFOX, Props.PROXY_HOST(), Props.PROXY_PORT());

    webClient.getOptions().setUseInsecureSSL(true);
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    webClient.setCssErrorHandler(new SilentCssErrorHandler());
    
    for (Entry<String, String> header: Global.standardHeaders.entrySet()) {
    	webClient.addRequestHeader(header.getKey(), header.getValue());
		}

    DefaultCredentialsProvider scp = new DefaultCredentialsProvider();
    scp.addCredentials(Props.PROXY_USERNAME(), Props.PROXY_PASSWORD(), Props.PROXY_HOST(), Props.PROXY_PORT(), null);
    webClient.setCredentialsProvider(scp);
	}

	/**
	 * Returns WebClient instance. 
	 * 
	 * Please note that: 
	 * 		If you need to execute a number of requests consecutively, 
	 * 		You need to call this method only once for your whole operations!
	 * 
	 * @return WebClient
	 */
	public static WebClient getClient() {
		if (webClient == null || webClient.getCurrentWindow().isClosed()) start();
		return webClient;
	}
	
	/**
	 * Should be called when the system is being shut down!
	 */
	public static void stop() {
		if (webClient != null) webClient.close();
	}
	
}
