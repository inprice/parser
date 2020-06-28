package io.inprice.scrapper.parser.consumer;

import io.inprice.scrapper.common.config.SysProps;

/**
 * Handles AVAILABLE status
 */
public class AvailableConsumer extends BaseCompetitorConsumer {

  public AvailableConsumer() {
    super("Available competitors consumer", SysProps.MQ_AVALIABLE_COMPETITORS_QUEUE());
  }

}
