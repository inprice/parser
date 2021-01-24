package io.inprice.parser.websites.fr;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Bonprix;

public class BonprixFR extends Bonprix {

  @Override
	public Country getCountry() {
		return Consts.Countries.FR;
	}

}
