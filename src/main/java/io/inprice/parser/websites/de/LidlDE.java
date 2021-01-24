package io.inprice.parser.websites.de;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Lidl;

public class LidlDE extends Lidl {

  @Override
	public Country getCountry() {
		return Consts.Countries.DE;
	}

}
