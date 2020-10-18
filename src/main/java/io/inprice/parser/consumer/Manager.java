package io.inprice.parser.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;
import io.inprice.common.models.Link;
import io.inprice.parser.config.Props;
import io.inprice.parser.helpers.RedisClient;

public class Manager {

  private static final Logger log = LoggerFactory.getLogger(Manager.class);

  private static RTopic topic;
  private static ExecutorService tPool;

  public static void start() {
    log.info("Manager is starting...");

    topic = RedisClient.createTopic(SysProps.REDIS_ACTIVE_LINKS_TOPIC());
    tPool = Executors.newFixedThreadPool(Props.ACTIVE_LINKS_CONSUMER_TPOOL_CAPACITY());

    topic.addListener(Link.class, new MessageListener<Link>(){
      public void onMessage(CharSequence channel, Link link) {
        tPool.submit(new ActiveLinkConsumer(link));
      };
    });

    log.info("Manager is started.");
  }

  public static void shutdown() {
    try {
      topic.removeAllListeners();
      tPool.shutdown();
      tPool.awaitTermination(SysProps.WAITING_TIME_FOR_TERMINATION(), TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("Thread pool termination is interrupted.", e);
    }
  }

}
