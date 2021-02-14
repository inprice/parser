package io.inprice.parser.websites.es;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Zalando;

public class ZalandoES extends Zalando {

  @Override
	public Country getCountry() {
		return Consts.Countries.ES;
	}

}
