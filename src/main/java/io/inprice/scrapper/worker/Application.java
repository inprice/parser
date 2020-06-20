package io.inprice.scrapper.worker;

import org.apache.http.client.config.CookieSpecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.scrapper.common.config.SysProps;
import io.inprice.scrapper.common.helpers.RabbitMQ;
import io.inprice.scrapper.common.meta.AppEnv;
import io.inprice.scrapper.worker.config.Props;
import io.inprice.scrapper.worker.consumer.AvailableConsumer;
import io.inprice.scrapper.worker.consumer.FailedConsumer;
import io.inprice.scrapper.worker.consumer.TobeClassifiedConsumer;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.ThreadPools;
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

      new TobeClassifiedConsumer().start();
      new AvailableConsumer().start();
      new FailedConsumer().start();

    }, "app-starter").start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("APPLICATION IS TERMINATING...");
      Global.isApplicationRunning = false;

      log.info(" - Thread pools are shutting down...");
      ThreadPools.shutdown();

      log.info(" - RabbitMQ connection is closing...");
      RabbitMQ.closeConnection();

      log.info(" - Unirest is shuting down...");
      Unirest.shutDown(true);

      log.info("ALL SERVICES IS DONE.");
    }, "shutdown-hook"));
  }

  private static void config() {
    if (SysProps.APP_ENV().equals(AppEnv.PROD)) {
      System.setProperty("http.proxyHost", Props.PROXY_HOST());
      System.setProperty("http.proxyPort", Props.PROXY_PORT());
    }

    Unirest.config()
      .socketTimeout(5 * 1000) //five second
      .connectTimeout(8 * 1000) //eight seconds
      .cookieSpec(CookieSpecs.STANDARD)
      .setDefaultHeader("Accept-Language", "en-US,en;q=0.5");
  }

}
