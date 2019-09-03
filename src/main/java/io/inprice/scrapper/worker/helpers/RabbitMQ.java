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

	private static final Properties properties = Beans.getSingleton(Properties.class);

	private static Channel channel;

	public static Channel getChannel() {
		if (!isChannelActive()) {
			synchronized (log) {
				if (!isChannelActive()) {
					final ConnectionFactory connectionFactory = new ConnectionFactory();
					connectionFactory.setHost(properties.getMQ_Host());
					connectionFactory.setPort(properties.getMQ_Port());
					connectionFactory.setUsername(properties.getMQ_Username());
					connectionFactory.setPassword(properties.getMQ_Password());

					try {
						Connection connection = connectionFactory.newConnection();
						channel = connection.createChannel();

						channel.exchangeDeclare(properties.getMQ_LinkExchange(), "topic");
						channel.exchangeDeclare(properties.getMQ_ChangeExchange(), "topic");

						channel.queueDeclare(properties.getMQ_NewLinksQueue(), true, false, false, null);
						channel.queueDeclare(properties.getMQ_FailedLinksQueue(), true, false, false, null);
						channel.queueDeclare(properties.getMQ_AvailableLinksQueue(), true, false, false, null);
						channel.queueDeclare(properties.getMQ_TobeAvailableLinksQueue(), true, false, false, null);

						channel.queueDeclare(properties.getMQ_StatusChangeQueue(), true, false, false, null);
						channel.queueDeclare(properties.getMQ_PriceChangeQueue(), true, false, false, null);

						channel.queueBind(properties.getMQ_NewLinksQueue(), properties.getMQ_LinkExchange(), properties.getMQ_NewLinksQueue() + ".#");
						channel.queueBind(properties.getMQ_FailedLinksQueue(), properties.getMQ_LinkExchange(),  properties.getMQ_FailedLinksQueue() + ".#");
						channel.queueBind(properties.getMQ_AvailableLinksQueue(), properties.getMQ_LinkExchange(),  properties.getMQ_AvailableLinksQueue() + ".#");
						channel.queueBind(properties.getMQ_TobeAvailableLinksQueue(), properties.getMQ_LinkExchange(),  properties.getMQ_TobeAvailableLinksQueue() + ".#");

						channel.queueBind(properties.getMQ_StatusChangeQueue(), properties.getMQ_ChangeExchange(),  properties.getMQ_StatusChangeQueue() + ".#");
						channel.queueBind(properties.getMQ_PriceChangeQueue(), properties.getMQ_ChangeExchange(),  properties.getMQ_PriceChangeQueue() + ".#");
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

	public static void publish(String queue, Serializable message) {
		publish(properties.getMQ_LinkExchange(), queue, message);
	}

	public static void publish(String exchange, String queue, Serializable message) {
		try {
			channel.basicPublish(exchange, queue, null, SerializationUtils.serialize(message));
		} catch (IOException e) {
			log.error("Failed to send a message to queue", e);
		}
	}

	public static boolean isChannelActive() {
		return (channel != null && channel.isOpen());
	}

}
