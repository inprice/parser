package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

public class AvailableLinksConsumer extends BaseLinkConsumer {

	public AvailableLinksConsumer() {
		super("Available and Renewed links consumer", Config.RABBITMQ_AVAILABLE_LINKS_QUEUE);
	}

}
