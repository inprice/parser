package io.inprice.parser.consumer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.time.StopWatch;
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
import io.inprice.parser.info.ParseCode;
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

  	Connection conn = RabbitMQ.createConnection(forWhichConsumer);
  	Channel channel = conn.createChannel();
  	ExecutorService tPool = Executors.newFixedThreadPool(queueDef.CAPACITY);

		Consumer consumer = new DefaultConsumer(channel) {
  		@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
  	  	tPool.execute(() -> {
					StopWatch watch = new StopWatch();
					watch.start();

	  			try {
						String message = new String(body);
						Link link = JsonConverter.fromJsonWithoutJsonIgnore(message, Link.class);
		
						LinkStatus oldStatus = link.getStatus();
				    BigDecimal oldPrice = link.getPrice();
	
				    ParseStatus newParseStatus = null;
		
				    if (link.getPlatform().getClassName() != null) {
				      try {
				        Class<?> clazz = Class.forName("io.inprice.parser.websites." + link.getPlatform().getClassName());
				        Website website = (Website) clazz.getConstructor().newInstance();
				        newParseStatus = website.check(link);
				      } catch (Exception e) {
				      	newParseStatus = new ParseStatus(ParseCode.OTHER_EXCEPTION, e.getMessage());
				        logger.error(link.getUrl(), e);
				      }
				    } else {
				    	newParseStatus = new ParseStatus(ParseCode.NOT_IMPLEMENTED, "Platform is null");
				      logger.warn("Platform is null! Status: {}, Url: {} ", link.getStatus(), link.getUrl());
				    }
				    
				    switch (newParseStatus.getCode()) {
				    	case OK: {
				    		link.setStatus(LinkStatus.AVAILABLE);
				    		break;
				    	}
				    	case NO_DATA: {
				    		link.setStatus(LinkStatus.NO_DATA);
				    		break;
				    	}
				    	case NOT_AVAILABLE: {
				    		link.setStatus(LinkStatus.NOT_AVAILABLE);
				    		break;
				    	}
				    	case NOT_FOUND: {
				    		link.setStatus(LinkStatus.NOT_FOUND);
				    		break;
				    	}
				    	case NOT_IMPLEMENTED: {
				    		link.setStatus(LinkStatus.TOBE_IMPLEMENTED);
				    		break;
				    	}
				  		/*------------------------------------------*/
				    	case IO_EXCEPTION: {
				    		link.setStatus(LinkStatus.NETWORK_ERROR);
				    		break;
				    	}
				    	case TIMEOUT_EXCEPTION: {
				    		link.setStatus(LinkStatus.TIMED_OUT);
				    		break;
				    	}
				    	case OTHER_EXCEPTION: {
				    		link.setStatus(LinkStatus.INTERNAL_ERROR);
				    		break;
				    	}
				  		/*------------------------------------------*/
							case HTTP_NOT_FOUND: {
				    		link.setStatus(LinkStatus.NOT_FOUND);
				    		break;
							}
				    	case HTTP_NOT_ALLOWED: {
				    		link.setStatus(LinkStatus.NOT_ALLOWED);
				    		break;
				    	}
							case HTTP_UNREACHABLE_SITE: {
				    		link.setStatus(LinkStatus.SITE_DOWN);
				    		break;
							}
				  		/*------------------------------------------*/
							default: {
								logger.warn("{} has unexpected problem: {}", link.getUrl(), link.getParseProblem());
							}
				    }
	
				    link.setParseCode(newParseStatus.getCode().name());
				    link.setParseProblem(StringUtils.clearErrorMessage(newParseStatus.getMessage()));
		
				    if (link.getStatus().equals(oldStatus) == false || link.getPrice().equals(oldPrice) == false) {
				    	StatusChangingLinksPublisher.publish(new LinkStatusChange(link, oldStatus, oldPrice));
				    }
	
						watch.stop();
						String logPart = String.format("%s: %s, Time: %dms", newParseStatus.getCode(), link.getPlatform().getName(), watch.getTime());
	
						if (newParseStatus.getCode().equals(ParseCode.OK)) {
							logger.info("{}, URL: {}", logPart, link.getUrl());
						} else {
							logger.warn("{}, Problem: {}, URL: {}", logPart, newParseStatus.getMessage(), link.getUrl());
						}
	  			} catch (Exception e) {
	    			try {
							channel.basicAck(envelope.getDeliveryTag(), false);
						} catch (IOException e1) { e1.printStackTrace(); }
	    			logger.error("Failed to handle active link", e);
	    		} finally {
	  				if (watch.isStopped() == false) watch.stop();
	    		}
  	  	});
			}
		};

		logger.info(forWhichConsumer + " is up and running.");
		channel.basicConsume(queueDef.NAME, true, consumer);
  }

}
