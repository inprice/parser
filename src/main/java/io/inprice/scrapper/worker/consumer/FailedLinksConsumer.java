package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Props;

/**
 * Handles links in SOCKET_ERROR and NETWORK_ERROR (for a certain times)
 */
public class FailedLinksConsumer extends BaseLinkConsumer {

  public FailedLinksConsumer() {
    super("Failed links consumer", Props.MQ_QUEUE_FAILED_LINKS());
  }

}
