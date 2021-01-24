package io.inprice.parser.websites.us;

import io.inprice.parser.helpers.Consts;
import io.inprice.parser.info.Country;
import io.inprice.parser.websites.xx.Ebay;

public class EbayUS extends Ebay {

  @Override
	public Country getCountry() {
		return Consts.Countries.US;
	}

}
