package io.inprice.parser.websites.es;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Lidl;

public class LidlES extends Lidl {

  @Override
	public Country getCountry() {
		return Consts.Countries.ES;
	}

}
