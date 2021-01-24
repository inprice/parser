package io.inprice.parser.websites.fr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Laredoute_FR_Test {

	private final Laredoute site = new Laredoute();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("500787340", link.getSku());
		assertEquals("Sommier de relaxation électrique", link.getName());
		assertEquals("431.20", link.getPrice().toString());
		assertEquals("REVERIE", link.getBrand());
		assertEquals("La Redoute", link.getSeller());
		assertEquals("Livraison à domicile : 39,00 €", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("515599021", link.getSku());
		assertEquals("Baskets N-5923", link.getName());
		assertEquals("59.94", link.getPrice().toString());
		assertEquals("adidas Originals", link.getBrand());
		assertEquals("La Redoute", link.getSeller());
		assertEquals("Livraison gratuite en point Relais Colis  dès 29€*", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("527882400", link.getSku());
		assertEquals("Lot de 20 serviettes papier champignon", link.getName());
		assertEquals("2.40", link.getPrice().toString());
		assertEquals("DOUCEUR D&#39;INTÉRIEUR", link.getBrand());
		assertEquals("1001 Kdo", link.getSeller());
		assertEquals("Livrable en point Relais Colis  : 3,90 €", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("500779771", link.getSku());
		assertEquals("Fauteuil de jardin, Joalie", link.getName());
		assertEquals("104.38", link.getPrice().toString());
		assertEquals("LA REDOUTE INTERIEURS", link.getBrand());
		assertEquals("La Redoute", link.getSeller());
		assertEquals("Livraison à domicile : 19,00 €", link.getShipment());
		assertNull(link.getSpecList());
	}

}
