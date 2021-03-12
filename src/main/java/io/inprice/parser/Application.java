package io.inprice.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.parser.consumer.ConsumerManager;
import io.inprice.parser.helpers.Global;
import kong.unirest.Unirest;

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
    new Thread(() -> {
      Global.isApplicationRunning = true;

      //config();
      ConsumerManager.start();

    }, "app-starter").start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("APPLICATION IS TERMINATING...");
      Global.isApplicationRunning = false;

      log.info(" - Thread pools are shutting down...");
      ConsumerManager.stop();

      if (Unirest.isRunning()) {
        log.info(" - Unirest is shuting down...");
        Unirest.shutDown(true);
      }

      log.info("ALL SERVICES IS DONE.");
    }, "shutdown-hook"));
  }

  /*
  private static void config() {
  	 unirest
    Unirest.config()
      .socketTimeout(SysProps.HTTP_CONNECTION_TIMEOUT() * 1000)
      .connectTimeout(SysProps.HTTP_CONNECTION_TIMEOUT() * 1000)
      .cookieSpec(CookieSpecs.STANDARD)
      .proxy(Props.PROXY_HOST(), Props.PROXY_PORT())
      .setDefaultHeader("Accept-Language", "en-US,en;q=0.5");

    //proxy
    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
    
    Authenticator.setDefault(new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
      	System.out.println("PASSWORD AUTH HIT!");
        if (getRequestorType().equals(RequestorType.PROXY)) {
          return new PasswordAuthentication(Props.PROXY_USERNAME(), (Props.PROXY_PASSWORD()).toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });
    
    log.info("Proxy is set!");

  }
   */

}
