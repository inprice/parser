package io.inprice.parser.helpers;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import io.inprice.parser.config.Props;

public class ProxyHelper {

	//not used!!!
	private static void setProxy() {
    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

    Authenticator.setDefault(new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType().equals(RequestorType.PROXY)) {
          return new PasswordAuthentication(Props.PROXY_USERNAME(), (Props.PROXY_PASSWORD()).toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });
	}
	
}
