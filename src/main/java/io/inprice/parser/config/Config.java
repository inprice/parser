package io.inprice.parser.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.inprice.common.config.BaseConfig;

public class Config extends BaseConfig {

	@JsonProperty("app")
	public App APP;

	@JsonProperty("proxy")
	public ProxyConf PROXY;

	@JsonProperty("queues")
	public Queues QUEUES;
	
}
