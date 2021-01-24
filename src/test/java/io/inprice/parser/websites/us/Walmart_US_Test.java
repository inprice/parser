package io.inprice.parser.websites.us;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Walmart_US_Test {

	private final Walmart site = new Walmart();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("55508481", link.getSku());
		assertEquals("Ozark Trail 12x12 Slant Leg Canopy", link.getName());
		assertEquals("49.20", link.getPrice().toString());
		assertEquals("Ozark Trail", link.getBrand());
		assertEquals("Walmart", link.getSeller());
		assertEquals("Free 2-day delivery", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("911905348", link.getSku());
		assertEquals("Hilasal Palm Island Fiber-Reaction Printed Beach Towel - 30 x 60 inches 12058", link.getName());
		assertEquals("9.95", link.getPrice().toString());
		assertEquals("Hilasal USA", link.getBrand());
		assertEquals("American Living Online", link.getSeller());
		assertEquals("Free delivery", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("257080810", link.getSku());
		assertEquals("POP TV: Stranger Things- 6\" Big Demogorgon", link.getName());
		assertEquals("13.76", link.getPrice().toString());
		assertEquals("Funko", link.getBrand());
		assertEquals("Calendars LLC", link.getSeller());
		assertEquals("Free delivery", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("568773858", link.getSku());
		assertEquals("Theragun liv Handheld Compact Percussive Therapy Device, Portable Muscle Massager", link.getName());
		assertEquals("299.00", link.getPrice().toString());
		assertEquals("Theragun", link.getBrand());
		assertEquals("Walmart", link.getSeller());
		assertEquals("Free delivery", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
