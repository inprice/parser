package io.inprice.scrapper.worker.consumer;

/**
 * Handles links in SOCKET_ERROR and NETWORK_ERROR (for a certain times)
 */
public class FailedLinksConsumer extends BaseLinkConsumer {

	public FailedLinksConsumer() {
		super("Failed links consumer", properties.getQueue_FailedLinks());
	}

}
