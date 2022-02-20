package io.inprice.parser.consumer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
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
import io.inprice.common.meta.Grup;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.utils.StringHelper;
import io.inprice.parser.info.AlternativeParser;
import io.inprice.parser.info.ParseCode;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.publisher.StatusChangingLinksPublisher;
import io.inprice.parser.websites.AbstractWebsite;

/**
 *
 * @since 2021-08-15
 * @author mdpinar
 */
class ActiveLinksConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ActiveLinksConsumer.class);

  private static final Map<String, AlternativeParser> alternativeParserMap = Map.of(
  		"xx.WalmartXX", new AlternativeParser("/PRD", "xx.WalmartXX_ALT")
  	);
  
  ActiveLinksConsumer(QueueDef queueDef) throws IOException {
  	String forWhichConsumer = "PAR-CON: " + queueDef.NAME;

  	Connection conn = RabbitMQ.createConnection(forWhichConsumer);
  	Channel channel = conn.createChannel();
  	ExecutorService tPool = Executors.newFixedThreadPool(queueDef.CAPACITY >= 1 && queueDef.CAPACITY <= 20 ? queueDef.CAPACITY : 3);

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
		
				    String className = link.getPlatform().getClassName();
				    if (className != null) {
				      try {

				      	//checks class name and a word in the url to determine if an alternative class must be used!
				      	AlternativeParser altPar = alternativeParserMap.get(className);
				      	if (altPar != null && link.getUrl().indexOf(altPar.getWordInUrl()) > 0) {
				      		className = altPar.getClassName();
				      	}

				      	Class<?> clazz = Class.forName("io.inprice.parser.websites." + className);
				        AbstractWebsite website = (AbstractWebsite) clazz.getConstructor().newInstance();
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
							case HTTP_OTHER_ERROR: {
				    		link.setStatus(LinkStatus.NETWORK_ERROR);
				    		break;
							}
				  		/*------------------------------------------*/
							default: {
								logger.warn("{} has unexpected problem: {}", link.getUrl(), link.getParseProblem());
							}
				    }

				    link.setParseCode(newParseStatus.getCode().name());
				    link.setParseProblem(StringHelper.clearErrorMessage(newParseStatus.getMessage()));

				    if (link.getGrup().equals(Grup.ACTIVE) == false 
		    		|| link.getStatus().equals(oldStatus) == false 
		    		|| link.getPrice().equals(oldPrice) == false) {
				    	StatusChangingLinksPublisher.publish(link);
				    }

						watch.stop();
						String logPart = String.format("%s : %dms | ", newParseStatus.getCode(), watch.getTime());

						if (newParseStatus.getCode().equals(ParseCode.OK)) {
							logger.info("{}, URL: {}", logPart, link.getUrl());
						} else {
							logger.warn("{}, Problem: {}, URL: {}", logPart, newParseStatus.getMessage(), link.getUrl());
						}
	  			} catch (Exception e) {
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
