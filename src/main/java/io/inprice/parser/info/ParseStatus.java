package io.inprice.parser.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ParseStatus {

	public static final ParseStatus PS_NO_DATA = new ParseStatus(ParseCode.NO_DATA, "No data");
	public static final ParseStatus PS_NOT_FOUND = new ParseStatus(ParseCode.NOT_FOUND, "Not found");

	private ParseCode code;
	private String message;

}
