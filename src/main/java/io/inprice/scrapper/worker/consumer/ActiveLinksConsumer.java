package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

public class ActiveLinksConsumer extends BaseLinkConsumer {

	public ActiveLinksConsumer() {
		super("Available links consumer", Config.RABBITMQ_AVAILABLE_LINKS_QUEUE);
	}

}
