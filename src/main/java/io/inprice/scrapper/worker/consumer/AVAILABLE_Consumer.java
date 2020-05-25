package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Props;

/**
 * Handles AVAILABLE status
 */
public class AVAILABLE_Consumer extends BaseLinkConsumer {

  public AVAILABLE_Consumer() {
    super("Available links consumer", Props.MQ_QUEUE_AVALIABLE_LINKS());
  }

}
