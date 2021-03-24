package io.inprice.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.parser.consumer.ConsumerManager;
import static io.inprice.parser.helpers.Global.*;

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
      isApplicationRunning = true;
      
      HTMLUNIT_POOL.setup();

      ConsumerManager.start();

    }, "app-starter").start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("APPLICATION IS TERMINATING...");
      isApplicationRunning = false;
      
      log.info(" - HtmlUnit pool is shutting down...");
      HTMLUNIT_POOL.shutdown();
      
      log.info(" - Thread pools are shutting down...");
      ConsumerManager.stop();

      log.info("ALL SERVICES IS DONE.");
    }, "shutdown-hook"));
  }

}
