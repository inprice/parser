package io.inprice.parser;

import static io.inprice.parser.helpers.Global.isApplicationRunning;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.parser.config.Props;
import io.inprice.parser.consumer.ConsumerManager;

/**
 * Entry point of the application.
 * 
 * @since 2019-04-20
 * @author mdpinar
 *
 */
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

    Authenticator.setDefault(new Authenticator() {
    	@Override
      protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType().equals(RequestorType.PROXY)) {
          return new PasswordAuthentication(Props.PROXY_USERNAME, Props.PROXY_PASSWORD.toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });

  	new Thread(() -> {
      isApplicationRunning = true;

      ConsumerManager.start();

    }, "app-starter").start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("APPLICATION IS TERMINATING...");
      isApplicationRunning = false;
      
      log.info(" - Thread pools are shutting down...");
      ConsumerManager.stop();

      log.info("ALL SERVICES IS DONE.");
    }, "shutdown-hook"));
  }

}
