package io.inprice.parser.websites.uk;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Apple;

public class AppleUK extends Apple {

  @Override
	public Country getCountry() {
		return Consts.Countries.UK;
	}

}
