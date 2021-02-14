package io.inprice.parser.websites.it;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Lidl_IT_Test {

	private final Website site = new LidlIT();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("25951", link.getSku());
		assertEquals("Pettine elettrico lisciante", link.getName());
		assertEquals("14.99", link.getPrice().toString());
		assertEquals("Pettine", link.getBrand());
		assertEquals("Lidl", link.getSeller());
		assertEquals("In-store pickup", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("25959", link.getSku());
		assertEquals("Dispositivo per massaggi sottovuoto", link.getName());
		assertEquals("22.99", link.getPrice().toString());
		assertEquals("Dispositivo", link.getBrand());
		assertEquals("Lidl", link.getSeller());
		assertEquals("In-store pickup", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("25984", link.getSku());
		assertEquals("Cuscino 50x80 cm", link.getName());
		assertEquals("9.99", link.getPrice().toString());
		assertEquals("Cuscino", link.getBrand());
		assertEquals("Lidl", link.getSeller());
		assertEquals("In-store pickup", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("26050", link.getSku());
		assertEquals("Robot aspirapolvere", link.getName());
		assertEquals("129.00", link.getPrice().toString());
		assertEquals("Robot", link.getBrand());
		assertEquals("Lidl", link.getSeller());
		assertEquals("In-store pickup", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
