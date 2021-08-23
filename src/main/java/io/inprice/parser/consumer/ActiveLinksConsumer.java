package io.inprice.parser.consumer;

import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.inprice.common.config.QueueDef;
import io.inprice.common.helpers.JsonConverter;
import io.inprice.common.helpers.RabbitMQ;
import io.inprice.common.info.LinkStatusChange;
import io.inprice.common.info.ParseStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.publisher.StatusChangingLinksPublisher;
import io.inprice.parser.websites.Website;

/**
 *
 * @since 2021-08-15
 * @author mdpinar
 */
class ActiveLinksConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ActiveLinksConsumer.class);
  
  ActiveLinksConsumer(QueueDef queueDef) throws IOException {
  	String forWhichConsumer = "PAR-CON: " + queueDef.NAME;

  	Connection conn = RabbitMQ.createConnection(forWhichConsumer, queueDef.CAPACITY);
  	Channel channel = conn.createChannel();

		Consumer consumer = new DefaultConsumer(channel) {
  		@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String inMessage = new String(body);
				Link link = JsonConverter.fromJson(inMessage, Link.class);
				
				ParseStatus newParseStatus = ParseStatus.PS_OK;
		    BigDecimal oldPrice = link.getPrice();

		    if (link.getPlatform() != null) {
		      try {
		        Class<?> clazz = Class.forName("io.inprice.parser.websites." + link.getPlatform().getClassName());
		        Website website = (Website) clazz.getConstructor().newInstance();
		        newParseStatus = website.check(link);
		      } catch (Exception e) {
		      	newParseStatus = new ParseStatus(ParseStatus.CODE_UNEXPECTED_EXCEPTION, e.getMessage());
		        logger.error(link.getUrl(), e);
		      }
		    } else {
		    	newParseStatus = new ParseStatus(ParseStatus.CODE_NOT_IMPLEMENTED, "Link's platform is null");
		      logger.warn("Website platform is null! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
		    }

		    StatusChangingLinksPublisher.publish(new LinkStatusChange(link, newParseStatus, oldPrice));
			}
		};

		logger.info(forWhichConsumer + " is up and running.");
		channel.basicConsume(queueDef.NAME, true, consumer);
  }

}
