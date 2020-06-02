package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.common.config.SysProps;

/**
 * Handles competitors in SOCKET_ERROR and NETWORK_ERROR (for a certain times)
 */
public class FailedConsumer extends BaseCompetitorConsumer {

  public FailedConsumer() {
    super("Failed competitors consumer", SysProps.MQ_FAILED_COMPETITORS_QUEUE());
  }

}
