package io.inprice.parser.websites.nl;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Apple;

public class AppleNL extends Apple {

  @Override
	public Country getCountry() {
		return Consts.Countries.NL;
	}

}
