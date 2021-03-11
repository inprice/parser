package io.inprice.parser.websites.es;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.xx.MediaMarkt;

public class MediaMarkt_ES_Test {

	private final Website site = new MediaMarkt() {};

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
		assertEquals("1299751", link.getSku());
		assertEquals("Memoria USB 64 GB - Sandisk 139789 Ultra Flair, USB 3.0, Velocidad hasta 150mb/sg", link.getName());
		assertEquals("9.99", link.getPrice().toString());
		assertEquals("SANDISK", link.getBrand());
		assertEquals("Media Markt", link.getSeller());
		assertEquals("más envío 1,99", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("1272050", link.getSku());
		assertEquals("Disco duro de 2TB - WD Elements, 2.5 pulgadas", link.getName());
		assertEquals("68.00", link.getPrice().toString());
		assertEquals("WESTERN DIGITAL", link.getBrand());
		assertEquals("Media Markt", link.getSeller());
		assertEquals("más envío 1,99", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("1275902", link.getSku());
		assertEquals("Cafetera - De Longhi EC7.1 Potencia 800W, Sistema Cappuccino, Tapón de seguridad", link.getName());
		assertEquals("64.90", link.getPrice().toString());
		assertEquals("DE LONGHI", link.getBrand());
		assertEquals("Media Markt", link.getSeller());
		assertEquals("más envío 2,99", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
		assertEquals("1405935", link.getSku());
		assertEquals("PS4 Marvel S Spider-Man", link.getName());
		assertEquals("24.90", link.getPrice().toString());
		assertEquals("SONY COMPUTER ENT. S.A. (SOFT)", link.getBrand());
		assertEquals("Media Markt", link.getSeller());
		assertEquals("más envío 1,99", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
