package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

public class NewLinksConsumer extends BaseLinkConsumer {

	public NewLinksConsumer() {
		super("New links consumer", Config.RABBITMQ_NEW_LINKS_QUEUE);
	}

}
