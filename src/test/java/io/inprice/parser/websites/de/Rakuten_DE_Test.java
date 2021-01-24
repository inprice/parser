package io.inprice.parser.websites.de;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Rakuten_DE_Test {

	private final Website site = new RakutenDE();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("56477395", link.getSku());
		assertEquals("Western Stars", link.getName());
		assertEquals("20.99", link.getPrice().toString());
		assertEquals("Sony Music Entertainment Germ", link.getBrand());
		assertEquals("buecher.de", link.getSeller());
		assertEquals("Free shipping", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("217988201", link.getSku());
		assertEquals("Xiaomi N4M340 Ninebot Plus 11 Zoll Electric Scooter Self Balancing Selbstbalancierendes Doppelräder",
		    link.getName());
		assertEquals("707.77", link.getPrice().toString());
		assertEquals("Xiaomi", link.getBrand());
		assertEquals("Shenzhen Shanghua E-Commerce Co", link.getSeller());
		assertEquals("Free shipping", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("1353202652-6613-1937", link.getSku());
		assertEquals("Energiespar Deckenventilator Eco Genuino Chrom Flügel Holz Natur", link.getName());
		assertEquals("489.00", link.getPrice().toString());
		assertEquals("CasaFan", link.getBrand());
		assertEquals("Tobias Krist", link.getSeller());
		assertEquals("Free shipping", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("00000182-500", link.getSku());
		assertEquals("Eisformen aus Silikon / Stieleisformen / Eisformen Eis am Stiel, Stieleisformen Silikon, rot",
		    link.getName());
		assertEquals("16.99", link.getPrice().toString());
		assertEquals("Zollner24", link.getBrand());
		assertEquals("Zollner24", link.getSeller());
		assertEquals("Free shipping", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
