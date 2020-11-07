package io.inprice.parser.helpers;

import java.math.BigDecimal;

import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;
import io.inprice.common.helpers.BaseRedisClient;
import io.inprice.common.info.StatusChange;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;

public class RedisClient {

  private static final Logger log = LoggerFactory.getLogger(RedisClient.class);

  private static BaseRedisClient baseClient;
  private static RTopic statusChangeTopic;

  static {
    baseClient = new BaseRedisClient();
    baseClient.open(() -> {
      statusChangeTopic = createTopic(SysProps.REDIS_STATUS_CHANGE_TOPIC());
    });
  }

  public static RTopic createTopic(String topic) {
    return baseClient.getClient().getTopic(topic);
  }

  public static void publishStatusChange(Link link, LinkStatus oldStatus, BigDecimal oldPrice) {
    if (baseClient.isHealthy()) {
      statusChangeTopic.publish(new StatusChange(link, oldStatus, oldPrice));
    } else {
      log.error("Redis seems not healty. Sending StatusChange message error! Status: {}, Url: {}", link.getStatus(), link.getUrl());
    }
  }

  public static void shutdown() {
    statusChangeTopic.removeAllListeners();
    baseClient.shutdown();
  }

}
