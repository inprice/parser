package io.inprice.parser.info;

public final class Country {

	private String code;
	private String name;
	private String lookup;

	public Country(String code, String name) {
		super();
		this.code = code;
		this.name = name;
		this.lookup = name.replaceAll(" ", "");
	}

	public Country(String code, String name, String lookup) {
		super();
		this.code = code;
		this.name = name;
		this.lookup = lookup;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public String getLookup() {
		return lookup;
	}

}
