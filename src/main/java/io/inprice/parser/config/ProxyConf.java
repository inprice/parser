package io.inprice.parser.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProxyConf {

	@JsonProperty("active")
	public Boolean ACTIVE = Boolean.FALSE;

	@JsonProperty("host")
	public String HOST;

	@JsonProperty("port")
	public Integer PORT;

	@JsonProperty("username")
	public String USERNAME;

	@JsonProperty("password")
	public String PASSWORD;
	
}
