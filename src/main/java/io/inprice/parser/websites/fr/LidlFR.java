package io.inprice.parser.websites.fr;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Lidl;

public class LidlFR extends Lidl {

  @Override
	public Country getCountry() {
		return Consts.Countries.FR;
	}

}
