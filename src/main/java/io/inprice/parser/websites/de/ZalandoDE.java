package io.inprice.parser.websites.de;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Zalando;

public class ZalandoDE extends Zalando {

  @Override
	public Country getCountry() {
		return Consts.Countries.DE;
	}

}
