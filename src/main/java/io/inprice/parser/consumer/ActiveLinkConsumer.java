package io.inprice.parser.consumer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.inprice.common.helpers.JsonConverter;
import io.inprice.common.helpers.Rabbit;
import io.inprice.common.info.LinkStatusChange;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.meta.QueueName;
import io.inprice.common.models.Link;
import io.inprice.common.utils.StringUtils;
import io.inprice.parser.websites.Website;

/**
 *
 * @since 2021-08-15
 * @author mdpinar
 */
class ActiveLinkConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ActiveLinkConsumer.class);
  
  ActiveLinkConsumer(QueueName queueName) throws IOException {
  	String forWhichConsumer = "Parser consumer: " + queueName.getName();

  	Connection consConnection = Rabbit.createConnection(forWhichConsumer, queueName.getCapacity());
  	Channel consChannel = consConnection.createChannel();

  	Consumer consumer = new DefaultConsumer(consChannel) {

  		@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String inMessage = new String(body, "UTF-8");
				Link link = JsonConverter.fromJson(inMessage, Link.class);
				
		  	LinkStatus oldStatus = link.getStatus();
		    BigDecimal oldPrice = link.getPrice();

		    if (link.getPlatform() != null) {
		      try {
		        Class<?> clazz = Class.forName("io.inprice.parser.websites." + link.getPlatform().getClassName());
		        Website website = (Website) clazz.getConstructor().newInstance();
		        website.check(link);
		      } catch (Exception e) {
		        link.setStatus(LinkStatus.INTERNAL_ERROR);
		        link.setProblem(StringUtils.clearErrorMessage(e.getMessage()));
		        link.setHttpStatus(500);
		        logger.error(link.getUrl(), e);
		      }
		    } else {
		      logger.warn("Website platform is null! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
		      link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
		      link.setProblem("NOT IMPLEMENTED YET");
		    }

		    //publishing status change, if any
		    //no worry, there some controls in Rabbit.createConnection() method to avoid resource leak!
		  	Connection publConnection = Rabbit.createConnection("Parser publisher: " + QueueName.STATUS_CHANGING_LINKS.getName());
		  	Channel publChannel = publConnection.createChannel();
		  	
		  	String outMessage = JsonConverter.toJson(new LinkStatusChange(link, oldStatus, oldPrice));
		  	publChannel.basicPublish("", QueueName.STATUS_CHANGING_LINKS.getName(), null, outMessage.getBytes());
		  	try {
					publChannel.close();
				} catch (IOException | TimeoutException e) {
	        logger.error("Failed to close a publisher channel of status changing link", e);
				}
			}
		};

		consChannel.basicConsume(queueName.getName(), true, consumer);
		logger.info(forWhichConsumer + " is up and running.");
  }

}
