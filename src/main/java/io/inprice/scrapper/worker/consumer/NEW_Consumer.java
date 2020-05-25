package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Props;

/**
 * Handles NEW and RENEWED links
 */
public class NEW_Consumer extends BaseLinkConsumer {

  public NEW_Consumer() {
    super("NEW links consumer", Props.MQ_QUEUE_NEW_LINKS());
  }

}
