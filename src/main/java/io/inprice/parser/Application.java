package io.inprice.parser;

import org.apache.http.client.config.CookieSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;
import io.inprice.common.meta.AppEnv;
import io.inprice.parser.config.Props;
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

      config();
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

  private static void config() {
    if (SysProps.APP_ENV().equals(AppEnv.PROD)) {
      System.setProperty("java.net.useSystemProxies", "true");
      System.setProperty("http.proxyHost", Props.PROXY_HOST());
      System.setProperty("http.proxyPort", Props.PROXY_PORT());
      System.setProperty("https.proxyHost", Props.PROXY_HOST());
      System.setProperty("https.proxyPort", Props.PROXY_PORT());
    }

    log.info("Proxy Host: {}", Props.PROXY_HOST());
    log.info("Proxy Port: {}", Props.PROXY_PORT());

    Unirest.config()
      .socketTimeout(SysProps.HTTP_CONNECTION_TIMEOUT() * 1000)
      .connectTimeout(SysProps.HTTP_CONNECTION_TIMEOUT() * 1000)
      .cookieSpec(CookieSpecs.STANDARD)
      .setDefaultHeader("Accept-Language", "en-US,en;q=0.5");
  }

}
