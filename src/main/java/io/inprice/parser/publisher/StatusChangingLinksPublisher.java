package io.inprice.parser.publisher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import io.inprice.common.helpers.JsonConverter;
import io.inprice.common.helpers.RabbitMQ;
import io.inprice.common.info.LinkStatusChange;
import io.inprice.parser.config.Props;

/**
 * 
 * @since 2021-08-19
 * @author mdpinar
 */
public class StatusChangingLinksPublisher {

  private static final Logger logger = LoggerFactory.getLogger(StatusChangingLinksPublisher.class);
	
	private static final Connection conn;

	static {
		conn = RabbitMQ.createConnection("parser-publisher: " + Props.getConfig().QUEUES.STATUS_CHANGING_LINKS.NAME);
	}

	public static void publish(LinkStatusChange change) {
  	try (Channel channel = conn.createChannel()) {
	  	String outMessage = JsonConverter.toJson(change);
	  	channel.basicPublish("", Props.getConfig().QUEUES.STATUS_CHANGING_LINKS.NAME, null, outMessage.getBytes());
  	} catch (IOException | TimeoutException e) {
      logger.error("Failed to publish status changing link", e);
		}
	}
	
}
