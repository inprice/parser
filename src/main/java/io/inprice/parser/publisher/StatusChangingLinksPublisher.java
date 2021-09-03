package io.inprice.parser.publisher;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import io.inprice.common.helpers.JsonConverter;
import io.inprice.common.helpers.RabbitMQ;
import io.inprice.common.models.Link;
import io.inprice.parser.config.Props;

/**
 * 
 * @since 2021-08-19
 * @author mdpinar
 */
public class StatusChangingLinksPublisher {

  private static final Logger logger = LoggerFactory.getLogger(StatusChangingLinksPublisher.class);
	
	private static Connection conn;
	private static Channel channel;

	static {
		try {
			conn = RabbitMQ.createConnection("PAR-PUB: " + Props.getConfig().QUEUES.STATUS_CHANGING_LINKS.NAME);
			channel = conn.createChannel();
		} catch (IOException e) {
	    logger.error("Failed to establish RabbitMQ connection", e);
		}
	}

	public static void publish(Link link) {
  	try {
	  	String message = JsonConverter.toJsonWithoutIgnoring(link);
	  	channel.basicPublish("", Props.getConfig().QUEUES.STATUS_CHANGING_LINKS.NAME, null, message.getBytes());
  	} catch (IOException e) {
      logger.error("Failed to publish status changing link", e);
		}
	}
	
}
