package io.inprice.parser.helpers;

import java.util.ArrayList;
import java.util.List;

import io.inprice.common.info.Pair;

public class SomeCountries {

	private static List<Pair<String, String>> LIST;
	
	static {
		LIST = new ArrayList<>();
		LIST.add(new Pair<String, String>("AU", "Australia"));
		LIST.add(new Pair<String, String>("AT", "Austria"));
		LIST.add(new Pair<String, String>("BE", "Belgium"));
		LIST.add(new Pair<String, String>("CA", "Canada"));
		LIST.add(new Pair<String, String>("CY", "Cyprus"));
		LIST.add(new Pair<String, String>("EE", "Estonia"));
		LIST.add(new Pair<String, String>("FI", "Finland"));
		LIST.add(new Pair<String, String>("FR", "France"));
		LIST.add(new Pair<String, String>("DE", "Germany"));
		LIST.add(new Pair<String, String>("GR", "Greece"));
		LIST.add(new Pair<String, String>("IS", "Iceland"));
		LIST.add(new Pair<String, String>("IE", "Ireland, Republic of"));
		LIST.add(new Pair<String, String>("IT", "Italy"));
		LIST.add(new Pair<String, String>("JP", "Japan"));
		LIST.add(new Pair<String, String>("LT", "Lithuania"));
		LIST.add(new Pair<String, String>("LU", "Luxembourg"));
		LIST.add(new Pair<String, String>("MT", "Malta"));
		LIST.add(new Pair<String, String>("NL", "Netherlands"));
		LIST.add(new Pair<String, String>("NZ", "New Zealand"));
		LIST.add(new Pair<String, String>("NO", "Norway"));
		LIST.add(new Pair<String, String>("PL", "Poland"));
		LIST.add(new Pair<String, String>("SK", "Slovakia"));
		LIST.add(new Pair<String, String>("SI", "Slovenia"));
		LIST.add(new Pair<String, String>("ES", "Spain"));
		LIST.add(new Pair<String, String>("SE", "Sweden"));
		LIST.add(new Pair<String, String>("CH", "Switzerland"));
		LIST.add(new Pair<String, String>("TR", "Turkey"));
		LIST.add(new Pair<String, String>("AE", "United Arab Emirates"));
		LIST.add(new Pair<String, String>("GB", "United Kingdom"));
		LIST.add(new Pair<String, String>("US", "United States"));
	}

	public static Pair<String, String> findOne() {
		return LIST.get((int) Math.floor(Math.random() * LIST.size()-1));
	}

}
