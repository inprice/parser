package io.inprice.scrapper.worker.helpers;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.scrapper.worker.config.Props;

public class RabbitMQ {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQ.class);

	private static Channel channel;

	public static Channel getChannel() {
		if (!isChannelActive()) {
			synchronized (log) {
				if (!isChannelActive()) {
					final ConnectionFactory connectionFactory = new ConnectionFactory();
					connectionFactory.setHost(Props.MQ_HOST());
					connectionFactory.setPort(Props.MQ_PORT());
					connectionFactory.setUsername(Props.MQ_USERNAME());
					connectionFactory.setPassword(Props.MQ_PASSWORD());

					try {
						Connection connection = connectionFactory.newConnection();
						channel = connection.createChannel();

						final String tobeAvailableLinksQueue = Props.MQ_ROUTING_TOBE_AVAILABLE_LINKS();
						final String statusChangeQueue = Props.MQ_ROUTING_STATUS_CHANGES();
						final String priceChangeQueue = Props.MQ_ROUTING_PRICE_CHANGES();

						final String newLinksQueue = Props.MQ_QUEUE_NEW_LINKS();
						final String availableLinksQueue = Props.MQ_QUEUE_AVALIABLE_LINKS();
						final String failedLinksQueue = Props.MQ_QUEUE_FAILED_LINKS();

						channel.exchangeDeclare(Props.MQ_EXCHANGE_LINKS(), "topic");
						channel.exchangeDeclare(Props.MQ_EXCHANGE_CHANGES(), "topic");
						channel.exchangeDeclare(Props.MQ_EXCHANGE_DEAD_LETTER(), "topic");

            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-dead-letter-exchange", Props.MQ_EXCHANGE_DEAD_LETTER());

						channel.queueDeclare(newLinksQueue, true, false, false, args);
						channel.queueDeclare(availableLinksQueue, true, false, false, args);
						channel.queueDeclare(failedLinksQueue, true, false, false, args);

						channel.queueDeclare(tobeAvailableLinksQueue, true, false, false, args);
						channel.queueDeclare(statusChangeQueue, true, false, false, args);
						channel.queueDeclare(priceChangeQueue, true, false, false, args);

						channel.queueBind(newLinksQueue, Props.MQ_EXCHANGE_LINKS(), newLinksQueue + ".#");
						channel.queueBind(availableLinksQueue, Props.MQ_EXCHANGE_LINKS(), availableLinksQueue + ".#");
						channel.queueBind(failedLinksQueue, Props.MQ_EXCHANGE_LINKS(), failedLinksQueue + ".#");

						channel.queueBind(tobeAvailableLinksQueue, Props.MQ_EXCHANGE_LINKS(), tobeAvailableLinksQueue + ".#");
						channel.queueBind(statusChangeQueue, Props.MQ_EXCHANGE_CHANGES(), statusChangeQueue + ".#");
						channel.queueBind(priceChangeQueue, Props.MQ_EXCHANGE_CHANGES(), priceChangeQueue + ".#");
					} catch (Exception e) {
						log.error("Failed to open RabbitMQ channel", e);
					}
				}
			}
		}

		return channel;
	}

	public static void closeChannel() {
		try {
			if (isChannelActive()) {
				channel.close();
			}
		} catch (IOException | TimeoutException e) {
			log.error("Error while RabbitMQ.channel is closed.", e);
		}
	}

	public static void publish(String routingKey, Serializable message) {
		publish(Props.MQ_EXCHANGE_LINKS(), routingKey, message);
	}

	public static void publish(String exchange, String routingKey, Serializable message) {
		try {
			channel.basicPublish(exchange, routingKey, null, SerializationUtils.serialize(message));
		} catch (IOException e) {
			log.error("Failed to send a message to queue", e);
		}
	}

	public static boolean isChannelActive() {
		return (channel != null && channel.isOpen());
	}

}
