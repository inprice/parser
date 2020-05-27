package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.common.config.SysProps;

/**
 * Handles AVAILABLE status
 */
public class AVAILABLE_Consumer extends BaseLinkConsumer {

  public AVAILABLE_Consumer() {
    super("Available links consumer", SysProps.MQ_AVALIABLE_LINKS_QUEUE());
  }

}
