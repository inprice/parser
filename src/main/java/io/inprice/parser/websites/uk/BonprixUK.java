package io.inprice.parser.websites.uk;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Bonprix;

public class BonprixUK extends Bonprix {

  @Override
	public Country getCountry() {
		return Consts.Countries.UK;
	}

}
