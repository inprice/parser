package io.inprice.parser.websites.fr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Rakuten_FR_Test {

	private final Website site = new RakutenFR();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("7311271629221", link.getSku());
		assertEquals("Sony Xperia 1 Dual SIM 128 Go Blanc", link.getName());
		assertEquals("809.00", link.getPrice().toString());
		assertEquals("Sony", link.getBrand());
		assertEquals("Importshop", link.getSeller());
		assertEquals("Livraison gratuite", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("8436571382764", link.getSku());
		assertEquals("Vélo Electrique Pliable Mr Urban Ebike 20' Black", link.getName());
		assertEquals("419.95", link.getPrice().toString());
		assertEquals("Moverace", link.getBrand());
		assertEquals("FLOATUP", link.getSeller());
		assertEquals("Livraison gratuite", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("5099747470929", link.getSku());
		assertEquals("HIStory-Past, Present And Future Book I", link.getName());
		assertEquals("3.22", link.getPrice().toString());
		assertEquals("Janet Jackson", link.getBrand());
		assertEquals("momox", link.getSeller());
		assertEquals("Livraison gratuite", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("0842776106179", link.getSku());
		assertEquals("Google Chromecast 3 - Récepteur multimédia numérique", link.getName());
		assertEquals("39.00", link.getPrice().toString());
		assertEquals("Google", link.getBrand());
		assertEquals("Boulanger", link.getSeller());
		assertEquals("Livraison gratuite", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
