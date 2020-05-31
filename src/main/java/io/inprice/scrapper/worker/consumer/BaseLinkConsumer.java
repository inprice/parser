package io.inprice.scrapper.worker.consumer;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.scrapper.common.config.SysProps;
import io.inprice.scrapper.common.helpers.JsonConverter;
import io.inprice.scrapper.common.helpers.RabbitMQ;
import io.inprice.scrapper.common.info.PriceUpdateInfo;
import io.inprice.scrapper.common.info.StatusChange;
import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.ThreadPools;
import io.inprice.scrapper.worker.websites.Website;

class BaseLinkConsumer {

  private static final Logger log = LoggerFactory.getLogger(BaseLinkConsumer.class);

  private static final String BASE_PACKAGE = "io.inprice.scrapper.worker.websites.";

  private String name;
  private String queueName;
  private Channel pubChannel;

  BaseLinkConsumer(String name, String queueName) {
    this.name = name;
    this.queueName = queueName;
    this.pubChannel = RabbitMQ.openChannel();
  }

  @SuppressWarnings("unchecked")
  public void start() {
    log.info("{} is running.", name);

    final Channel conChannel = RabbitMQ.openChannel();

    final Consumer consumer = new DefaultConsumer(conChannel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
          final byte[] body) {
        ThreadPools.WORKER_POOL.submit(() -> {
          Link oldState = JsonConverter.fromJson(new String(body), Link.class);
          Link newState = JsonConverter.fromJson(new String(body), Link.class);

          if (newState.getWebsiteClassName() == null) {
            newState.setStatus(LinkStatus.RENEWED);
          } else {
            try {
              Class<Website> clazz = (Class<Website>) Class.forName(BASE_PACKAGE + newState.getWebsiteClassName());
              Constructor<Website> ctor = clazz.getConstructor(Link.class);
              Website website = ctor.newInstance(newState);
              website.check();
              conChannel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
              log.error("Failed to find the website", e);
              newState.setStatus(LinkStatus.CLASS_PROBLEM);
              try {
                conChannel.basicNack(envelope.getDeliveryTag(), false, false);
              } catch (IOException e1) {
                log.error("Failed to send a message to dlx", e1);
              }
            }
          }
          sendToQueue(oldState, newState);
        });
      }
    };

    try {
      conChannel.basicConsume(queueName, false, consumer);
    } catch (IOException e) {
      log.error("Error in setting up active links consumer to pull tasks from queue", e);
    }
  }

  private void sendToQueue(Link oldState, Link newState) {
    // status change
    if (!oldState.getStatus().equals(newState.getStatus())) {
      if (newState.getStatus().equals(LinkStatus.AVAILABLE)) {
        // the consumer class is in Manager, TobeAvailableLinksConsumer
        RabbitMQ.publishLink(pubChannel, SysProps.MQ_TOBE_AVAILABLE_LINKS_ROUTING(), JsonConverter.toJson(newState));
      } else {
        // the consumer class is in Manager, StatusChangeConsumer
        StatusChange change = new StatusChange(newState, oldState.getStatus());
        RabbitMQ.publish(pubChannel, SysProps.MQ_CHANGES_EXCHANGE(), SysProps.MQ_STATUS_CHANGES_ROUTING(), JsonConverter.toJson(change));
      }
    } else {
      // price change
      if (oldState.getPrice().compareTo(newState.getPrice()) != 0) {
        // the consumer class is in Manager, LinkPriceChangeConsumer
        PriceUpdateInfo pui = new PriceUpdateInfo(newState);
        RabbitMQ.publish(pubChannel, SysProps.MQ_CHANGES_EXCHANGE(), SysProps.MQ_PRICE_CHANGES_ROUTING(), JsonConverter.toJson(pui));
      }
    }
    // else, do nothing. we already set last_check time of the link to indicate is being cared
  }

}
