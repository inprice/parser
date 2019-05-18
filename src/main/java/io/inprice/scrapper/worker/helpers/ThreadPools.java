package io.inprice.scrapper.worker.helpers;

import io.inprice.scrapper.common.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPools {

	public static final ExecutorService WORKER_POOL;

	static {
		WORKER_POOL = new ThreadPoolExecutor(
			Config.TPOOLS_WORKER_CAPACITY,
			Config.TPOOLS_WORKER_CAPACITY,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>()
		);
	}

}
