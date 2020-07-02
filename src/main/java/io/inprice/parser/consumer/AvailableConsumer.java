package io.inprice.parser.consumer;

import io.inprice.common.config.SysProps;

/**
 * Handles AVAILABLE status
 */
public class AvailableConsumer extends BaseCompetitorConsumer {

  public AvailableConsumer() {
    super("Available competitors consumer", SysProps.MQ_AVALIABLE_COMPETITORS_QUEUE());
  }

}
