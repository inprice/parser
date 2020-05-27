package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.common.config.SysProps;

/**
 * Handles NEW and RENEWED links
 */
public class NEW_Consumer extends BaseLinkConsumer {

  public NEW_Consumer() {
    super("NEW links consumer", SysProps.MQ_NEW_LINKS_QUEUE());
  }

}
