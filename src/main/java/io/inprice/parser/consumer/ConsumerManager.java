package io.inprice.parser.consumer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.QueueDef;
import io.inprice.parser.config.Props;

public class ConsumerManager {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerManager.class);

  public static void start() {
    logger.info("Consumer manager is starting...");

    try {
    	List<QueueDef> activeLinksQueues = Props.getConfig().QUEUES.ACTIVE_LINKS;

    	if (CollectionUtils.isNotEmpty(activeLinksQueues)) {
    		for (QueueDef queue: activeLinksQueues) {
    			if (BooleanUtils.isTrue(queue.ACTIVE)) new ActiveLinksConsumer(queue);
    		}
    	}
			logger.info("Consumer manager is started.");
		} catch (IOException e) {
			logger.error("Failed to start consumer manager", e);
		}
  }

}
