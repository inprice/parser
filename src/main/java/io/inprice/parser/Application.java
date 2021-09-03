package io.inprice.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.helpers.RabbitMQ;
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

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
		Thread.currentThread().setName("main");

		//will ignore system errors stemming from gecko driver!
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				//ignored!
			}
		}));

		RabbitMQ.start(Props.getConfig().RABBIT_CONF);
    logger.info(" - RabbitMQ is started.");

    ConsumerManager.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("APPLICATION IS TERMINATING...");
      
      logger.info(" - RabbitMQ is shutting down...");
      RabbitMQ.stop();

      logger.info("ALL SERVICES IS DONE.");
    }, "shutdown-hook"));
  }

}
