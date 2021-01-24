package io.inprice.parser.websites.au;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Ebay;

public class EbayAU extends Ebay {

  @Override
	public Country getCountry() {
		return Consts.Countries.AU;
	}

}
