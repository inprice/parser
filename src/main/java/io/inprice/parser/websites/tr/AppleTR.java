package io.inprice.parser.websites.tr;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Apple;

public class AppleTR extends Apple {

  @Override
	public Country getCountry() {
		return Consts.Countries.TR_DE;
	}

}
