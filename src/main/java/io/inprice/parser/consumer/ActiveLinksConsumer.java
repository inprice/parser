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
import io.inprice.parser.info.ParseStatus;
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
  			try {
					String message = new String(body);
					Link link = JsonConverter.fromJsonWithoutJsonIgnore(message, Link.class);
	
					LinkStatus oldStatus = link.getStatus();
			    BigDecimal oldPrice = link.getPrice();
			    int oldParseCode = link.getParseCode();
	
			    ParseStatus newParseStatus = new ParseStatus(link.getParseCode(), link.getParseProblem());
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
			    	newParseStatus = new ParseStatus(ParseStatus.CODE_NOT_IMPLEMENTED, "Link platform is null");
			      logger.warn("Website platform is null! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
			    }
			    
			    if (newParseStatus.getCode() != oldParseCode) {
				    switch (newParseStatus.getCode()) {
				    	case ParseStatus.CODE_OK: {
				    		link.setStatus(LinkStatus.AVAILABLE);
				    		break;
				    	}
				    	case ParseStatus.CODE_NO_DATA: {
				    		link.setStatus(LinkStatus.NO_DATA);
				    		break;
				    	}
				    	case ParseStatus.CODE_NOT_AVAILABLE: {
				    		link.setStatus(LinkStatus.NOT_AVAILABLE);
				    		break;
				    	}
				    	case ParseStatus.CODE_NOT_IMPLEMENTED: {
				    		link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
				    		break;
				    	}
				  		/*------------------------------------------*/
				    	case ParseStatus.CODE_IO_EXCEPTION: {
				    		link.setStatus(LinkStatus.NETWORK_ERROR);
				    		break;
				    	}
				    	case ParseStatus.CODE_TIMEOUT_EXCEPTION: {
				    		link.setStatus(LinkStatus.TIMED_OUT);
				    		break;
				    	}
				    	case ParseStatus.CODE_UNEXPECTED_EXCEPTION: {
				    		link.setStatus(LinkStatus.INTERNAL_ERROR);
				    		break;
				    	}
				  		/*------------------------------------------*/
							case ParseStatus.HTTP_CODE_NOT_FOUND: {
				    		link.setStatus(LinkStatus.NOT_FOUND);
				    		break;
							}
				    	case ParseStatus.HTTP_CODE_NOT_ALLOWED: {
				    		link.setStatus(LinkStatus.NOT_ALLOWED);
				    		break;
				    	}
							case ParseStatus.HTTP_CODE_UNREACHABLE_SITE: {
				    		link.setStatus(LinkStatus.SITE_DOWN);
				    		break;
							}
				  		/*------------------------------------------*/
							default: {
								if (link.getParseCode() >= 400) { //other http errors
									link.setStatus(LinkStatus.NETWORK_ERROR);
								} else if (link.getParseCode() > 0) { //unexpected errors which must not be thrown
									logger.warn("%s has unexpected Code: %d and Problem: %s", link.getUrl(), link.getParseCode(), link.getParseProblem());
								}
							}
				    }
	
				    link.setParseCode(newParseStatus.getCode());
				    link.setParseProblem(StringUtils.clearErrorMessage(newParseStatus.getMessage()));
			    }
	
			    if (link.getStatus().equals(oldStatus) == false || link.getPrice().equals(oldPrice) == false) {
			    	StatusChangingLinksPublisher.publish(new LinkStatusChange(link, oldStatus, oldPrice));
			    }
    		} catch (Exception e) {
    			channel.basicAck(envelope.getDeliveryTag(), false);
    			logger.error("Failed to handle active link", e);
    		}
			}
		};

		logger.info(forWhichConsumer + " is up and running.");
		channel.basicConsume(queueDef.NAME, true, consumer);
  }

}
