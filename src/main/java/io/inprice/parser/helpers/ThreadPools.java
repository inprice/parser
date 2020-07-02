package io.inprice.parser.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.inprice.common.config.SysProps;

public class ThreadPools {

  private static final Logger log = LoggerFactory.getLogger(ThreadPools.class);

  public static final ExecutorService WORKER_POOL;
  private static final List<ExecutorService> registry;

  static {
    WORKER_POOL = Executors.newFixedThreadPool(2);

    registry = new ArrayList<>();
    registry.add(WORKER_POOL);
  }

  public static void shutdown() {
    for (ExecutorService pool : registry) {
      try {
        pool.shutdown();
        pool.awaitTermination(SysProps.WAITING_TIME_FOR_TERMINATION(), TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        log.error("Thread pool termination is interrupted.", e);
      }
    }
  }

}
