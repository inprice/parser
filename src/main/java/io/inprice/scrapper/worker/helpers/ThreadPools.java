package io.inprice.scrapper.worker.helpers;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.worker.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPools {

	private static final Logger log = new Logger(ThreadPools.class);

	public static final ExecutorService WORKER_POOL;

	private static final List<ExecutorService> registry;

	static {
		WORKER_POOL = Executors.newFixedThreadPool(2);

		registry = new ArrayList<>();
		registry.add(WORKER_POOL);
	}

	public static void shutdown() {
		for (ExecutorService pool: registry) {
			try {
				pool.shutdown();
				pool.awaitTermination(Config.WAITING_TIME_FOR_AWAIT_TERMINATION, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.error("Thread pool termination is interrupted.", e);
			}
		}
	}

}
