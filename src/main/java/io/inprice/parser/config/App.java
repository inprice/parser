package io.inprice.parser.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.inprice.common.config.BaseSystem;

public class App extends BaseSystem {

	@JsonProperty("browserProfile")
	public String BROWSER_PROFILE;

}
