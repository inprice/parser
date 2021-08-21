package io.inprice.parser.config;

import com.google.gson.annotations.SerializedName;

import io.inprice.common.config.BaseConfig;

public class Config extends BaseConfig {

	@SerializedName("proxy")
	public ProxyConf PROXY;

	@SerializedName("queues")
	public Queues QUEUES;
	
}
