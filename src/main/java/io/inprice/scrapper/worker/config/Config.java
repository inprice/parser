package io.inprice.scrapper.worker.config;

public class Config {

	// RabbitMQ
	public static final String RABBITMQ_HOST;
	public static final int RABBITMQ_PORT;
	public static final String RABBITMQ_USERNAME;
	public static final String RABBITMQ_PASSWORD;

	// Exchange
	public static final String RABBITMQ_LINK_EXCHANGE;
	public static final String RABBITMQ_CHANGE_EXCHANGE;

	// Queues
	public static final String RABBITMQ_NEW_LINKS_QUEUE;
	public static final String RABBITMQ_ACTIVE_LINKS_QUEUE;
	public static final String RABBITMQ_FAILED_LINKS_QUEUE;

	public static final String RABBITMQ_STATUS_CHANGE_QUEUE;
	public static final String RABBITMQ_PRICE_CHANGE_QUEUE;
	public static final String RABBITMQ_ACTIVATED_LINKS_QUEUE;

	// Thread Pools
	public static final int TPOOLS_WORKER_CAPACITY;

	// Waiting times
	public static final long WAITING_TIME_FOR_AWAIT_TERMINATION;

	static {
		RABBITMQ_HOST = getOrDefault("RABBITMQ_HOST", "localhost");
		RABBITMQ_PORT = getOrDefault("RABBITMQ_PORT", 5672);
		RABBITMQ_USERNAME = getOrDefault("RABBITMQ_USERNAME", "guest");
		RABBITMQ_PASSWORD = getOrDefault("RABBITMQ_PASSWORD", "guest");

		RABBITMQ_LINK_EXCHANGE = getOrDefault("RABBITMQ_LINK_EXCHANGE", "links");
		RABBITMQ_CHANGE_EXCHANGE = getOrDefault("RABBITMQ_CHANGE_EXCHANGE", "changes");

		//minutely
		RABBITMQ_NEW_LINKS_QUEUE = getOrDefault("RABBITMQ_NEW_LINKS_QUEUE", "new-links");
		RABBITMQ_ACTIVE_LINKS_QUEUE = getOrDefault("RABBITMQ_ACTIVE_LINKS_QUEUE", "active-links");
		RABBITMQ_FAILED_LINKS_QUEUE = getOrDefault("RABBITMQ_FAILED_LINKS_QUEUE", "socket-errors");

		//different
		RABBITMQ_STATUS_CHANGE_QUEUE = getOrDefault("RABBITMQ_STATUS_CHANGE_QUEUE", "status-change");
		RABBITMQ_PRICE_CHANGE_QUEUE = getOrDefault("RABBITMQ_PRICE_CHANGE_QUEUE", "price-change");
		RABBITMQ_ACTIVATED_LINKS_QUEUE = getOrDefault("RABBITMQ_ACTIVATED_LINKS_QUEUE", "activated-links");

		TPOOLS_WORKER_CAPACITY = getOrDefault("TPOOLS_WORKER_CAPACITY", 2);
		WAITING_TIME_FOR_AWAIT_TERMINATION = getOrDefault("WTF_AWAIT_TERMINATION", 30000L);
	}

	private static String getOrDefault(String key, String defauld) {
		String val = System.getenv(key);
		if (val != null && val.trim().length() > 0) return val;
		return defauld;
	}

	private static int getOrDefault(String key, int defauld) {
		String val = System.getenv(key);
		if (val != null && val.trim().length() > 0) return Integer.parseInt(val.trim());
		return defauld;
	}

	private static long getOrDefault(String key, long defauld) {
		String val = System.getenv(key);
		if (val != null && val.trim().length() > 0) return Long.parseLong(val.trim());
		return defauld;
	}
}
