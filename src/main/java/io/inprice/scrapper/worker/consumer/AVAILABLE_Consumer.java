package io.inprice.scrapper.worker.consumer;

/**
 * Handles AVAILABLE status
 */
public class AVAILABLE_Consumer extends BaseLinkConsumer {

	public AVAILABLE_Consumer() {
		super("Available links consumer", properties.getMQ_AvailableLinksQueue());
	}

}
