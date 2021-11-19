package io.inprice.parser.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.inprice.common.config.AppBase;

public class App extends AppBase {

	@JsonProperty("browserProfile")
	public String BROWSER_PROFILE;

}
