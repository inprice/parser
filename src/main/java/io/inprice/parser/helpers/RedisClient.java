package io.inprice.parser.helpers;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;
import io.inprice.common.info.StatusChange;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;

public class RedisClient {

  private static final Logger log = LoggerFactory.getLogger(RedisClient.class);

  private static boolean isHealthy;
  private static RedissonClient client;

  private static RTopic statusChangeTopic;

  static {
    final String redisPass = SysProps.REDIS_PASSWORD();
    Config config = new Config();
    config
      .useSingleServer()
      .setAddress(String.format("redis://%s:%d", SysProps.REDIS_HOST(), SysProps.REDIS_PORT()))
      .setPassword(!StringUtils.isBlank(redisPass) ? redisPass : null)
      .setConnectionPoolSize(10)
      .setConnectionMinimumIdleSize(1)
      .setIdleConnectionTimeout(5000)
      .setTimeout(5000);

    while (!isHealthy && Global.isApplicationRunning) {
      try {
        client = Redisson.create(config);
        statusChangeTopic = client.getTopic(SysProps.REDIS_STATUS_CHANGE_TOPIC());
        isHealthy = true;
      } catch (Exception e) {
        log.error("Failed to connect to Redis server, trying again in 3 seconds!", e.getMessage());
        try {
          Thread.sleep(3000);
        } catch (InterruptedException ignored) { }
      }
    }
        
  }

  public static RTopic createTopic(String topic) {
    return client.getTopic(topic);
  }

  public static void publishStatusChange(Link link, LinkStatus oldStatus, BigDecimal oldPrice) {
    if (isHealthy) {
      statusChangeTopic.publish(new StatusChange(link, oldStatus, oldPrice));
    } else {
      log.warn("Redis not healty, StatusChange message failed to send!");
    }
  }

  public static void shutdown() {
    if (client != null) {
      client.shutdown();
    } else {
      log.warn("No redis client found!");
    }
  }

}
