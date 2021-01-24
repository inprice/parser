package io.inprice.parser.websites.es;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_ES_Test {

	private final Website site = new ZalandoES();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("C1852F00Z-Q11", link.getSku());
		assertEquals("Monedero", link.getName());
		assertEquals("38.95", link.getPrice().toString());
		assertEquals("Calvin Klein", link.getBrand());
		assertEquals("Zalando", link.getSeller());
		assertEquals(
		    "Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables",
		    link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("CL612M00S-B11", link.getSku());
		assertEquals("WALLABEE - Zapatos de vestir", link.getName());
		assertEquals("135.95", link.getPrice().toString());
		assertEquals("Clarks Originals", link.getBrand());
		assertEquals("Zalando", link.getSeller());
		assertEquals(
		    "Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables",
		    link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("EV451Q00W-G11", link.getSku());
		assertEquals("Mochila", link.getName());
		assertEquals("20.25", link.getPrice().toString());
		assertEquals("Even&Odd", link.getBrand());
		assertEquals("Zalando", link.getSeller());
		assertEquals(
		    "Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables",
		    link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("GP021C0BY-A11", link.getSku());
		assertEquals("EYELET - Vestido camisero", link.getName());
		assertEquals("48.95", link.getPrice().toString());
		assertEquals("GAP", link.getBrand());
		assertEquals("Zalando", link.getSeller());
		assertEquals(
		    "Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables",
		    link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
