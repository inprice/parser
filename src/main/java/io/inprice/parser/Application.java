package io.inprice.parser;

import static io.inprice.parser.helpers.Global.isApplicationRunning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.helpers.Rabbit;
import io.inprice.parser.consumer.ConsumerManager;

/**
 * Entry point of the application.
 * 
 * @since 2019-04-20
 * @author mdpinar
 *
 */
public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
  	new Thread(() -> {
      isApplicationRunning = true;
      
      Rabbit.start();
      logger.info(" - RabbitMQ is started.");

      ConsumerManager.start();

    }, "app-starter").start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("APPLICATION IS TERMINATING...");
      isApplicationRunning = false;
      
      logger.info(" - RabbitMQ is shutting down...");
      Rabbit.stop();

      logger.info("ALL SERVICES IS DONE.");
    }, "shutdown-hook"));
  }

}
