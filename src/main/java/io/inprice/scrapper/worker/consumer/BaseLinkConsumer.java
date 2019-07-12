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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
                            Class<Website> clazz = (Class<Website>) Class.forName(newState.getWebsiteClassName());
                            Constructor<Website> ctor = clazz.getConstructor(Link.class);
                            Website website = ctor.newInstance(newState);
                            website.check();
                        } catch (ClassCastException | IllegalAccessException | InstantiationException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
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
        //status change
        if (! oldState.getStatus().equals(newState.getStatus())) {
            if (newState.getStatus().equals(Status.AVAILABLE)) {
                RabbitMQ.publish(Config.RABBITMQ_LINK_EXCHANGE, Config.RABBITMQ_AVAILABLE_LINKS_QUEUE, Converter.fromObject(newState)); //the consumer class is in Master, AvailableLinksConsumer
            } else {
                StatusChange change = new StatusChange(newState, newState.getStatus());
                RabbitMQ.publish(Config.RABBITMQ_CHANGE_EXCHANGE, Config.RABBITMQ_STATUS_CHANGE_QUEUE, Converter.fromObject(change)); //the consumer class is in Master, StatusChangeConsumer
            }
        } else {
            //price change
            if (oldState.getPrice().compareTo(newState.getPrice()) != 0) {
                PriceChange change = new PriceChange(newState.getId(), newState.getProductId(), newState.getPrice());
                RabbitMQ.publish(Config.RABBITMQ_CHANGE_EXCHANGE, Config.RABBITMQ_PRICE_CHANGE_QUEUE, Converter.fromObject(change)); //the consumer class is in Master, LinkPriceChangeConsumer
            }
        }

    }

}
