package io.inprice.parser.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.RedisClient;

public class ConsumerManager {

  private static final Logger log = LoggerFactory.getLogger(ConsumerManager.class);

  private static RTopic topic;
  private static ExecutorService tPool;

  public static void start() {
    log.info("Consumer manager is starting...");

    tPool = Executors.newFixedThreadPool(SysProps.TPOOL_LINK_CONSUMER_CAPACITY());
    
    topic = RedisClient.createTopic(SysProps.REDIS_ACTIVE_LINKS_TOPIC());
    topic.addListener(Link.class, (channel, link) -> tPool.submit(new ConsumerActiveLinks(link)));

    log.info("Consumer manager is started.");
  }

  public static void stop() {
    try {
      topic.removeAllListeners();
      tPool.shutdown();
      tPool.awaitTermination(SysProps.WAITING_TIME_FOR_TERMINATION(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("Thread pool termination is interrupted.", e);
    }
  }

}
