package io.inprice.scrapper.worker;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.worker.consumer.AVAILABLE_Consumer;
import io.inprice.scrapper.worker.consumer.FailedLinksConsumer;
import io.inprice.scrapper.worker.consumer.NEW_Consumer;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.RabbitMQ;
import io.inprice.scrapper.worker.helpers.ThreadPools;

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
			Global.isApplicationRunning = true;

			new NEW_Consumer().start();
			new AVAILABLE_Consumer().start();
			new FailedLinksConsumer().start();

		}, "app-starter").start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("APPLICATION IS TERMINATING...");
			Global.isApplicationRunning = false;

			log.info(" - Thread pools are shutting down...");
			ThreadPools.shutdown();

			log.info(" - RabbitMQ connection is closing...");
			RabbitMQ.closeChannel();

			log.info("ALL SERVICES IS DONE.");
		},"shutdown-hook"));
	}

}
