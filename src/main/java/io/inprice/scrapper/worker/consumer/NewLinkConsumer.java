package io.inprice.scrapper.worker.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.inprice.scrapper.common.config.Config;
import io.inprice.scrapper.common.helpers.Converter;
import io.inprice.scrapper.common.helpers.RabbitMQ;
import io.inprice.scrapper.common.info.LinkStatusChange;
import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;

import io.inprice.scrapper.worker.websites.Website;
import io.inprice.scrapper.worker.helpers.ThreadPools;

import java.io.IOException;

public class NewLinkConsumer {

	private static final Logger log = new Logger(NewLinkConsumer.class);

	public static void start() {
		log.info("NewLinkConsumer is running.");

		final Consumer taskConsumer = new DefaultConsumer(RabbitMQ.getChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, final byte[] body) {
				try {
					ThreadPools.WORKER_POOL.submit(() -> {
						Link link = Converter.toObject(body);
						LinkStatus oldStatus = link.getStatus();
						try {
							Class<Website> resolverClass = (Class<Website>) Class.forName(link.getWebsiteClassName());
							Website website = resolverClass.newInstance();
							website.check(link);

							//TODO: bu kisimda basarili bir sekilde parse edilen NEW statusundeki linkler manager tarafindan ACTIVE statusune gecirilmek uzere kuyruga eklenmeliler

						} catch (ClassCastException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
							log.error(e);
							link.setStatus(LinkStatus.CLASS_PROBLEM);
						}
						if (! link.getStatus().equals(oldStatus)) {
							try {
								LinkStatusChange change = new LinkStatusChange(link, link.getStatus());
								RabbitMQ.getChannel().basicPublish(Config.RABBITMQ_LINK_EXCHANGE, Config.RABBITMQ_STATUS_CHANGE_QUEUE, null, Converter.fromObject(change));
							} catch (IOException e) {
								log.error("Failed to send a status change event", e);
							}
						}
					});
				} catch (Exception e) {
					log.error("Error in submitting Tasks into ThreadPool", e);
				}
			}
		};

		try {
			//TODO: hatalı queue, duzeltlmeli
			RabbitMQ.getChannel().basicConsume(Config.RABBITMQ_NEW_LINKS_QUEUE, true, taskConsumer);
		} catch (IOException e) {
			log.error("Error in setting up a consumer to pull tasks from queue", e);
		}
	}

}
