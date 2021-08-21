package io.inprice.parser.config;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import io.inprice.common.config.QueueDef;

public class Queues {

	@SerializedName("statusChangingLinks")
	public QueueDef STATUS_CHANGING_LINKS;

	@SerializedName("activeLinks")
	public List<QueueDef> ACTIVE_LINKS;

}
