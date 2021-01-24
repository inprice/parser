package io.inprice.parser.websites.fr;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Apple;

public class AppleFR extends Apple {

  @Override
	public Country getCountry() {
		return Consts.Countries.FR;
	}

}
