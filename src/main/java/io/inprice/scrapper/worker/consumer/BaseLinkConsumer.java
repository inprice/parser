package io.inprice.scrapper.worker.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.inprice.scrapper.common.helpers.Converter;
import io.inprice.scrapper.common.info.PriceChange;
import io.inprice.scrapper.common.info.StatusChange;
import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.config.Config;
import io.inprice.scrapper.worker.helpers.RabbitMQ;
import io.inprice.scrapper.worker.helpers.ThreadPools;
import io.inprice.scrapper.worker.websites.Website;

import java.io.IOException;

class BaseLinkConsumer {

    private static final Logger log = new Logger(BaseLinkConsumer.class);

    private String name;
    private String queueName;

    BaseLinkConsumer(String name, String queueName) {
        this.name = name;
        this.queueName = queueName;
    }

    public void start() {
        log.info("%s is running.", name);

        final Consumer consumer = new DefaultConsumer(RabbitMQ.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, final byte[] body) {
                try {
                    ThreadPools.WORKER_POOL.submit(() -> {
                        Link oldState = Converter.toObject(body);
                        Link newState = Converter.toObject(body);

                        try {
                            Class<Website> resolverClass = (Class<Website>) Class.forName(newState.getWebsiteClassName());
                            Website website = resolverClass.newInstance();
                            website.check(newState);
                        } catch (ClassCastException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                            log.error(e);
                            newState.setStatus(Status.CLASS_PROBLEM);
                        }
                        sendToQueue(oldState, newState);
                    });
                } catch (Exception e) {
                    log.error("Error in submitting tasks into ThreadPool", e);
                }
            }
        };

        try {
            RabbitMQ.getChannel().basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            log.error("Error in setting up active links consumer to pull tasks from queue", e);
        }
    }

    private void sendToQueue(Link oldState, Link newState) {
        try {
            //becomes active
            if (! newState.getActivated() && newState.getStatus().equals(Status.ACTIVE)) {
                RabbitMQ.getChannel().basicPublish(Config.RABBITMQ_LINK_EXCHANGE, Config.RABBITMQ_ACTIVATED_LINKS_QUEUE, null, Converter.fromObject(newState));

            //changes status
            } else if (! oldState.getStatus().equals(newState.getStatus())) {
                StatusChange change = new StatusChange(newState, newState.getStatus());
                RabbitMQ.getChannel().basicPublish(Config.RABBITMQ_LINK_EXCHANGE, Config.RABBITMQ_STATUS_CHANGE_QUEUE, null, Converter.fromObject(change));

            //changes price
            } else if (! oldState.getPrice().equals(newState.getPrice())) {
                PriceChange change = new PriceChange(newState.getId(), newState.getProductId(), newState.getPrice());
                RabbitMQ.getChannel().basicPublish(Config.RABBITMQ_LINK_EXCHANGE, Config.RABBITMQ_PRICE_CHANGE_QUEUE, null, Converter.fromObject(change));
            }
        } catch (IOException e) {
            log.error("Failed to send a message to queue", e);
        }
    }

}