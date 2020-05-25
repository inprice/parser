package io.inprice.scrapper.worker.consumer;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.scrapper.common.info.PriceUpdateInfo;
import io.inprice.scrapper.common.info.StatusChange;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.config.Props;
import io.inprice.scrapper.worker.helpers.RabbitMQ;
import io.inprice.scrapper.worker.helpers.ThreadPools;
import io.inprice.scrapper.worker.websites.Website;

class BaseLinkConsumer {

  private static final Logger log = LoggerFactory.getLogger(BaseLinkConsumer.class);

  private static final String BASE_PACKAGE = "io.inprice.scrapper.worker.websites.";

  private String name;
  private String queueName;

  BaseLinkConsumer(String name, String queueName) {
    this.name = name;
    this.queueName = queueName;
  }

  @SuppressWarnings("unchecked")
  public void start() {
    log.info("{} is running.", name);

    final Consumer consumer = new DefaultConsumer(RabbitMQ.getChannel()) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
          final byte[] body) {
        try {
          ThreadPools.WORKER_POOL.submit(() -> {
            Link oldState = SerializationUtils.deserialize(body);
            Link newState = SerializationUtils.deserialize(body);

            if (newState.getWebsiteClassName() == null) {
              newState.setStatus(Status.RENEWED);
            } else {
              try {
                Class<Website> clazz = (Class<Website>) Class.forName(BASE_PACKAGE + newState.getWebsiteClassName());
                Constructor<Website> ctor = clazz.getConstructor(Link.class);
                Website website = ctor.newInstance(newState);
                website.check();
                RabbitMQ.getChannel().basicAck(envelope.getDeliveryTag(), false);
              } catch (Exception e) {
                log.error("Failed to find the website", e);
                newState.setStatus(Status.CLASS_PROBLEM);
                try {
                  RabbitMQ.getChannel().basicNack(envelope.getDeliveryTag(), false, false);
                } catch (IOException e1) {
                  log.error("Failed to send a message to dlq", e1);
                }
              }
            }
            sendToQueue(oldState, newState);
          });
        } catch (Exception e) {
          log.error("Failed to submit tasks into ThreadPool", e);
        }
      }
    };

    try {
      RabbitMQ.getChannel().basicConsume(queueName, false, consumer);
    } catch (IOException e) {
      log.error("Error in setting up active links consumer to pull tasks from queue", e);
    }
  }

  private void sendToQueue(Link oldState, Link newState) {
    // status change
    if (!oldState.getStatus().equals(newState.getStatus())) {
      if (newState.getStatus().equals(Status.AVAILABLE)) {
        // the consumer class is in Master, TobeAvailableLinksConsumer
        RabbitMQ.publish(Props.MQ_ROUTING_TOBE_AVAILABLE_LINKS(), newState);
      } else {
        // the consumer class is in Master, StatusChangeConsumer
        StatusChange change = new StatusChange(newState, oldState.getStatus());
        RabbitMQ.publish(Props.MQ_EXCHANGE_CHANGES(), Props.MQ_ROUTING_STATUS_CHANGES(), change);
      }
    } else {
      // price change
      if (oldState.getPrice().compareTo(newState.getPrice()) != 0) {
        // the consumer class is in Master, LinkPriceChangeConsumer
        PriceUpdateInfo pui = new PriceUpdateInfo(newState);
        RabbitMQ.publish(Props.MQ_EXCHANGE_CHANGES(), Props.MQ_ROUTING_PRICE_CHANGES(), pui);
      }
    }
    // else, do nothing. we already set last_check time of the link to indicate that
    // it is cared
  }

}
