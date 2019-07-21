package io.inprice.scrapper.worker.consumer;

import io.inprice.scrapper.worker.config.Config;

/**
 * Handles NEW and RENEWED links
 */
public class NEW_Consumer extends BaseLinkConsumer {

	public NEW_Consumer() {
		super("NEW and RENEWED links consumer", Config.MQ_NEW_LINKS_QUEUE);
	}

}
