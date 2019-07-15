package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

/**
 * Handles AVAILABLE status
 */
public class AVAILABLE_Consumer extends BaseLinkConsumer {

	public AVAILABLE_Consumer() {
		super("Available links consumer", Config.RABBITMQ_AVAILABLE_LINKS_QUEUE);
	}

}
