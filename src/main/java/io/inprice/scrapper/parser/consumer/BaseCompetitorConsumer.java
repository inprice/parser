package io.inprice.scrapper.parser.consumer;

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
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.helpers.ThreadPools;
import io.inprice.scrapper.parser.websites.Website;

class BaseCompetitorConsumer {

  private static final Logger log = LoggerFactory.getLogger(BaseCompetitorConsumer.class);

  private static final String BASE_PACKAGE = "io.inprice.scrapper.parser.websites.";

  private String name;
  private String queueName;
  private Channel pubChannel;

  BaseCompetitorConsumer(String name, String queueName) {
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
          Competitor oldState = JsonConverter.fromJson(new String(body), Competitor.class);
          Competitor newState = JsonConverter.fromJson(new String(body), Competitor.class);

          if (newState.getWebsiteClassName() == null) {
            newState.setStatus(CompetitorStatus.TOBE_RENEWED);
          } else {
            try {
              Class<Website> clazz = (Class<Website>) Class.forName(BASE_PACKAGE + newState.getWebsiteClassName());
              Constructor<Website> ctor = clazz.getConstructor(Competitor.class);
              Website website = ctor.newInstance(newState);
              website.check();
              conChannel.basicAck(envelope.getDeliveryTag(), false);
            } catch (Exception e) {
              log.error("Failed to find the website", e);
              newState.setStatus(CompetitorStatus.CLASS_PROBLEM);
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
      log.error("Error in setting up active competitors consumer to pull tasks from queue", e);
    }
  }

  private void sendToQueue(Competitor oldState, Competitor newState) {
    // status change
    if (!oldState.getStatus().equals(newState.getStatus())) {
      if (newState.getStatus().equals(CompetitorStatus.AVAILABLE)) {
        if (newState.getProductId() == null) {
          // the consumer class is in API, ProductCreationFromLinkConsumer
          RabbitMQ.publish(pubChannel, SysProps.MQ_CHANGES_EXCHANGE(), SysProps.MQ_PRODUCT_CREATIONS_ROUTING(), JsonConverter.toJson(newState));
        } else {
          // the consumer class is in Manager, TobeAvailableCompetitorsConsumer
          RabbitMQ.publishCompetitor(pubChannel, SysProps.MQ_TOBE_AVAILABLE_COMPETITORS_ROUTING(), JsonConverter.toJson(newState));
        }
      } else {
        // the consumer class is in Manager, StatusChangeConsumer
        StatusChange change = new StatusChange(newState, oldState.getStatus());
        RabbitMQ.publish(pubChannel, SysProps.MQ_CHANGES_EXCHANGE(), SysProps.MQ_STATUS_CHANGES_ROUTING(), JsonConverter.toJson(change));
      }
    } else {
      // price change
      if (oldState.getPrice().compareTo(newState.getPrice()) != 0) {
        // the consumer class is in Manager, CompetitorPriceChangeConsumer
        PriceUpdateInfo pui = new PriceUpdateInfo(newState);
        RabbitMQ.publish(pubChannel, SysProps.MQ_CHANGES_EXCHANGE(), SysProps.MQ_PRICE_CHANGES_ROUTING(), JsonConverter.toJson(pui));
      }
    }
    // else, do nothing. we have already set last_check time of the competitor to indicate it is being cared
  }

}
