package io.inprice.scrapper.worker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

	private static final Logger log = LoggerFactory.getLogger(Properties.class);

	private final java.util.Properties prop;

	Properties() {
		prop = new java.util.Properties();

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				log.error("Unable to find config.properties in class path!");
				return;
			}
			prop.load(input);
		} catch (IOException e) {
			log.error("Failed to load config.properties", e);
		}
	}

	public boolean isRunningForTests() {
		String runningAt = prop.getProperty("app.running-at", "prod");
		return runningAt.equals("test");
	}

	public String getMQ_Host() {
		return prop.getProperty("mq.host", "localhost");
	}

	public int getMQ_Port() {
		return getOrDefault("mq.port", 5672);
	}

	public String getMQ_Username() {
		return prop.getProperty("mq.username", "guest");
	}

	public String getMQ_Password() {
		return prop.getProperty("mq.password", "guest");
	}

	public String getMQ_LinkExchange() {
		return prop.getProperty("mq.exchange.link", "links");
	}

	public String getMQ_ChangeExchange() {
		return prop.getProperty("mq.exchange.change", "changes");
	}

	public String getRoutingKey_TobeAvailableLinks() {
		return prop.getProperty("routingKey.for.tobe-available-links", "tobe-available-links");
	}

	public String getRoutingKey_StatusChange() {
		return prop.getProperty("routingKey.for.status-change", "status-change");
	}

	public String getRoutingKey_PriceChange() {
		return prop.getProperty("routingKey.for.price-change", "price-change");
	}

	public String getQueue_NewLinks() {
		return prop.getProperty("queue.of.new-links", "new-links");
	}

	/*
	 * the difference between AVAILABLE_LINKS_QUEUE and TOBE_AVAILABLE_LINKS_QUEUE is that
	 * AVAILABLE_LINKS are already in AVAILABLE status
	 * TOBE_AVAILABLE_LINKS are in different status and are about to switch to AVAILABLE.
	 */
	public String getQueue_AvailableLinks() {
		return prop.getProperty("queue.of.available-links", "available-links");
	}

	public String getQueue_FailedLinks() {
		return prop.getProperty("queue.of.failed-links", "failed-links");
	}

	public int getWT_ForAwaitTermination() {
		return getOrDefault("wt.for.await-termination", 30000);
	}

	private int getOrDefault(String key, int defauld) {
		String val = prop.getProperty(key, "" + defauld);
		return Integer.parseInt(val.trim());
	}

}
