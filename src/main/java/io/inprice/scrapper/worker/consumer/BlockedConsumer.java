package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.common.config.SysProps;

/**
 * Handles blocked competitors by its own website
 */
public class BlockedConsumer extends BaseCompetitorConsumer {

  public BlockedConsumer() {
    super("Blocked competitors consumer", SysProps.MQ_BLOCKED_COMPETITORS_QUEUE());
  }

}
