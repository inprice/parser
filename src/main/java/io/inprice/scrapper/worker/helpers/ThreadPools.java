package io.inprice.scrapper.worker.helpers;

import io.inprice.scrapper.common.helpers.Beans;
import io.inprice.scrapper.worker.config.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPools {

	private static final Logger log = LoggerFactory.getLogger(ThreadPools.class);

	private static final Properties properties = Beans.getSingleton(Properties.class);

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
				pool.awaitTermination(properties.getWTF_AwaitTermination(), TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.error("Thread pool termination is interrupted.", e);
			}
		}
	}

}
