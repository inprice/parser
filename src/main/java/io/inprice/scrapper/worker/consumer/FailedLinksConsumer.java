package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.common.config.SysProps;

/**
 * Handles links in SOCKET_ERROR and NETWORK_ERROR (for a certain times)
 */
public class FailedLinksConsumer extends BaseLinkConsumer {

  public FailedLinksConsumer() {
    super("Failed links consumer", SysProps.MQ_FAILED_LINKS_QUEUE());
  }

}
