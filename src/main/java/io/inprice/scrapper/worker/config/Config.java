package io.inprice.scrapper.worker.config;

public class Config {

	// RabbitMQ
	public static final String MQ_HOST;
	public static final int MQ_PORT;
	public static final String MQ_USERNAME;
	public static final String MQ_PASSWORD;

	// Exchange
	public static final String MQ_LINK_EXCHANGE;
	public static final String MQ_CHANGE_EXCHANGE;

	// Queues
	public static final String MQ_NEW_LINKS_QUEUE;
	public static final String MQ_FAILED_LINKS_QUEUE;

	/*
	 * the difference between AVAILABLE_LINKS_QUEUE and TOBE_AVAILABLE_LINKS_QUEUE is that
	 * AVAILABLE_LINKS are already in AVAILABLE status
	 * TOBE_AVAILABLE_LINKS are in different status and are about to switch to AVAILABLE.
	 */
	public static final String MQ_AVAILABLE_LINKS_QUEUE;
	public static final String MQ_TOBE_AVAILABLE_LINKS_QUEUE;

	public static final String MQ_STATUS_CHANGE_QUEUE;
	public static final String MQ_PRICE_CHANGE_QUEUE;

	// Waiting times
	public static final long WAITING_TIME_FOR_AWAIT_TERMINATION;

	static {
		MQ_HOST = getOrDefault("MQ_HOST", "localhost");
		MQ_PORT = getOrDefault("MQ_PORT", 5672);
		MQ_USERNAME = getOrDefault("MQ_USERNAME", "guest");
		MQ_PASSWORD = getOrDefault("MQ_PASSWORD", "guest");

		MQ_LINK_EXCHANGE = getOrDefault("MQ_LINK_EXCHANGE", "links");
		MQ_CHANGE_EXCHANGE = getOrDefault("MQ_CHANGE_EXCHANGE", "changes");

		//minutely
		MQ_NEW_LINKS_QUEUE = getOrDefault("MQ_NEW_LINKS_QUEUE", "new.links");
		MQ_FAILED_LINKS_QUEUE = getOrDefault("MQ_FAILED_LINKS_QUEUE", "failed.links");
		MQ_AVAILABLE_LINKS_QUEUE = getOrDefault("MQ_AVAILABLE_LINKS_QUEUE", "available.links");
		MQ_TOBE_AVAILABLE_LINKS_QUEUE = getOrDefault("MQ_TOBE_AVAILABLE_LINKS_QUEUE", "tobe-available.links");

		//different
		MQ_STATUS_CHANGE_QUEUE = getOrDefault("MQ_STATUS_CHANGE_QUEUE", "status.change");
		MQ_PRICE_CHANGE_QUEUE = getOrDefault("MQ_PRICE_CHANGE_QUEUE", "price.change");

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
