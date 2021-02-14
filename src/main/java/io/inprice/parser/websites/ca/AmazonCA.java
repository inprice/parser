package io.inprice.parser.websites.ca;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Amazon;

public class AmazonCA extends Amazon {

  @Override
	public Country getCountry() {
		return Consts.Countries.CA;
	}

}
