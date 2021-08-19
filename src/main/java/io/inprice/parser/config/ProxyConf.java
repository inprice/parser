package io.inprice.parser.config;

import com.google.gson.annotations.SerializedName;

public class ProxyConf {

	@SerializedName("active")
	public Boolean ACTIVE = Boolean.FALSE;

	@SerializedName("host")
	public String HOST;

	@SerializedName("port")
	public Integer PORT;

	@SerializedName("username")
	public String USERNAME;

	@SerializedName("password")
	public String PASSWORD;
	
}
