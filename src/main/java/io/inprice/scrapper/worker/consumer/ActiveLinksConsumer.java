package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

public class ActiveLinksConsumer extends BaseLinkConsumer {

	public ActiveLinksConsumer() {
		super("Active links consumer", Config.RABBITMQ_ACTIVE_LINKS_QUEUE);
	}

}
