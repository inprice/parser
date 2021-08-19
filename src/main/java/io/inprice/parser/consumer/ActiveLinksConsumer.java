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
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.utils.StringUtils;
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
  	String forWhichConsumer = "parser-consumer: " + queueDef.NAME;

  	try (Connection conn = RabbitMQ.createConnection(forWhichConsumer, queueDef.CAPACITY);
  			Channel channel = conn.createChannel()) {

  		Consumer consumer = new DefaultConsumer(channel) {
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
	
			    //publishes status change
			    StatusChangingLinksPublisher.publish(new LinkStatusChange(link, oldStatus, oldPrice));
				}
			};
	
			logger.info(forWhichConsumer + " is up and running.");
			channel.basicConsume(queueDef.NAME, true, consumer);
  	} catch (Exception e) {
			logger.error("Failed to connect rabbitmq server", e);
		}
  }

}
