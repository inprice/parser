package io.inprice.parser.websites.it;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Apple;

public class AppleIT extends Apple {

  @Override
	public Country getCountry() {
		return Consts.Countries.IT;
	}

}
