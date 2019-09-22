package io.inprice.scrapper.worker.helpers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.inprice.scrapper.common.helpers.Beans;
import io.inprice.scrapper.worker.config.Properties;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQ.class);
	private static final Properties props = Beans.getSingleton(Properties.class);

	private static Channel channel;

	public static Channel getChannel() {
		if (!isChannelActive()) {
			synchronized (log) {
				if (!isChannelActive()) {
					final ConnectionFactory connectionFactory = new ConnectionFactory();
					connectionFactory.setHost(props.getMQ_Host());
					connectionFactory.setPort(props.getMQ_Port());
					connectionFactory.setUsername(props.getMQ_Username());
					connectionFactory.setPassword(props.getMQ_Password());

					try {
						Connection connection = connectionFactory.newConnection();
						channel = connection.createChannel();

						final String tobeAvailableLinksQueue = props.getRoutingKey_TobeAvailableLinks();
						final String statusChangeQueue = props.getRoutingKey_StatusChange();
						final String priceChangeQueue = props.getRoutingKey_PriceChange();

						final String newLinksQueue = props.getQueue_NewLinks();
						final String availableLinksQueue = props.getQueue_AvailableLinks();
						final String failedLinksQueue = props.getQueue_FailedLinks();

						channel.exchangeDeclare(props.getMQ_LinkExchange(), "topic");
						channel.exchangeDeclare(props.getMQ_ChangeExchange(), "topic");

						channel.queueDeclare(newLinksQueue, true, false, false, null);
						channel.queueDeclare(availableLinksQueue, true, false, false, null);
						channel.queueDeclare(failedLinksQueue, true, false, false, null);

						channel.queueDeclare(tobeAvailableLinksQueue, true, false, false, null);
						channel.queueDeclare(statusChangeQueue, true, false, false, null);
						channel.queueDeclare(priceChangeQueue, true, false, false, null);

						channel.queueBind(newLinksQueue, props.getMQ_LinkExchange(), newLinksQueue + ".#");
						channel.queueBind(availableLinksQueue, props.getMQ_LinkExchange(), availableLinksQueue + ".#");
						channel.queueBind(failedLinksQueue, props.getMQ_LinkExchange(), failedLinksQueue + ".#");

						channel.queueBind(tobeAvailableLinksQueue, props.getMQ_LinkExchange(), tobeAvailableLinksQueue + ".#");
						channel.queueBind(statusChangeQueue, props.getMQ_ChangeExchange(), statusChangeQueue + ".#");
						channel.queueBind(priceChangeQueue, props.getMQ_ChangeExchange(), priceChangeQueue + ".#");
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
		publish(props.getMQ_LinkExchange(), routingKey, message);
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
