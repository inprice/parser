package io.inprice.parser.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class HttpStatus {
	
	public static final HttpStatus OK = new HttpStatus(200, null);
	public static final HttpStatus NOT_FOUND = new HttpStatus(404, "Not found!");
	public static final HttpStatus BLOCKED = new HttpStatus(403, "Site is blocked!");

	private int code;
	private String message;

}
