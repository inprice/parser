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

	public String getMQ_NewLinksQueue() {
		return prop.getProperty("mq.queue.new-links", "new-links");
	}

	/*
	 * the difference between AVAILABLE_LINKS_QUEUE and TOBE_AVAILABLE_LINKS_QUEUE is that
	 * AVAILABLE_LINKS are already in AVAILABLE status
	 * TOBE_AVAILABLE_LINKS are in different status and are about to switch to AVAILABLE.
	 */
	public String getMQ_AvailableLinksQueue() {
		return prop.getProperty("mq.queue.available-links", "available-links");
	}

	public String getMQ_TobeAvailableLinksQueue() {
		return prop.getProperty("mq.queue.tobe-available-links", "tobe-available-links");
	}

	public String getMQ_FailedLinksQueue() {
		return prop.getProperty("mq.queue.failed-links", "failed-links");
	}

	public String getMQ_StatusChangeQueue() {
		return prop.getProperty("mq.queue.status-change", "status-change");
	}

	public String getMQ_PriceChangeQueue() {
		return prop.getProperty("mq.queue.price-change", "price-change");
	}

	public int getWTF_AwaitTermination() {
		return getOrDefault("wtf.await-termination", 30000);
	}

	private int getOrDefault(String key, int defauld) {
		String val = prop.getProperty(key, "" + defauld);
		return Integer.parseInt(val.trim());
	}

}
