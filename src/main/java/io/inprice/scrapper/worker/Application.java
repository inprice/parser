package io.inprice.scrapper.worker;

import io.inprice.scrapper.common.config.Config;
import io.inprice.scrapper.common.helpers.RabbitMQ;
import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.worker.consumer.NewLinkConsumer;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.ThreadPools;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Entry point of the application.
 * 
 * @since 2019-04-20
 * @author mdpinar
 *
 */
public class Application {

	private static final Logger log = new Logger(Application.class);

	public static void main(String[] args) {
		new Thread(() -> {
			Global.isRunning = true;
			NewLinkConsumer.start();
		}, "task-processor").start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Global.isRunning = false;

			try {
				log.info("RabbitMQ connection is closing...");
				RabbitMQ.getChannel().abort();
				log.info("RabbitMQ is closed.");
			} catch (IOException e) {
				log.info("Rabbit abortion is interrupted.");
			}

			try {
				log.info("Thread pool is shutting down...");
				ThreadPools.WORKER_POOL.shutdown();
				ThreadPools.WORKER_POOL.awaitTermination(Config.WAITING_TIME_FOR_AWAIT_TERMINATION, TimeUnit.MILLISECONDS);
				log.info("Thread pool is shut down.");
			} catch (InterruptedException e) {
				log.info("Thread pool termination is interrupted.");
			}

			shutdown();
		}));
	}

	public static void shutdown() {
		log.info("TaskProcessor is shut down.");
	}

}
