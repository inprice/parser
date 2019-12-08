package io.inprice.scrapper.worker.consumer;

/**
 * Handles NEW and RENEWED links
 */
public class NEW_Consumer extends BaseLinkConsumer {

	public NEW_Consumer() {
		super("NEW links consumer", properties.getQueue_NewLinks());
	}

}
