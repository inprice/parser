package io.inprice.parser.websites.ca;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Apple;

public class AppleCA extends Apple {

  @Override
	public Country getCountry() {
		return Consts.Countries.CA;
	}

}
