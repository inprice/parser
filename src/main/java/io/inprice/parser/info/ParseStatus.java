package io.inprice.parser.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ParseStatus {

	public static final int CODE_OK = 0;

	public static final int CODE_NO_DATA = 10;
	public static final int CODE_NOT_FOUND = 11;
	public static final int CODE_NOT_AVAILABLE = 12;
	public static final int CODE_NOT_IMPLEMENTED = 13;

	public static final int CODE_IO_EXCEPTION = 20;
	public static final int CODE_TIMEOUT_EXCEPTION = 21;
	public static final int CODE_UNEXPECTED_EXCEPTION = 22;

	public static final int HTTP_CODE_NOT_FOUND = 404;
	public static final int HTTP_CODE_NOT_ALLOWED = 403;
	public static final int HTTP_CODE_UNREACHABLE_SITE = 503;
	
	public static final ParseStatus PS_OK = new ParseStatus(CODE_OK, null);
	public static final ParseStatus PS_NO_DATA = new ParseStatus(CODE_NO_DATA, "No data");
	public static final ParseStatus PS_NOT_FOUND = new ParseStatus(CODE_NOT_FOUND, "Not found");

	private int code;
	private String message;

}
