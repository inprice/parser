package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

public class FailedLinksConsumer extends BaseLinkConsumer {

	public FailedLinksConsumer() {
		super("Failed links consumer", Config.RABBITMQ_FAILED_LINKS_QUEUE);
	}

}
