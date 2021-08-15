package io.inprice.parser.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.meta.QueueName;

public class ConsumerManager {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerManager.class);

  public static void start() {
    logger.info("Consumer manager is starting...");

    try {
    	new ActiveLinkConsumer(QueueName.DEFAULT_LINKS_CAP1);
    	new ActiveLinkConsumer(QueueName.DEFAULT_LINKS_CAP3);

			logger.info("Consumer manager is started.");
		} catch (IOException e) {
			logger.error("Failed to start consumer manager", e);
		}
  }

}
