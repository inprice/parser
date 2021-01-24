package io.inprice.parser.websites.uk;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Argos_UK_Test {

	private final Argos site = new Argos();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("1344466", link.getSku());
		assertEquals("Amazon Echo Show 5 - Sandstone", link.getName());
		assertEquals("79.99", link.getPrice().toString());
		assertEquals("Amazon Echo", link.getBrand());
		assertEquals("Argos", link.getSeller());
		assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("3450925", link.getSku());
		assertEquals("3 Burner Propane Gas BBQ with Side Burner", link.getName());
		assertEquals("90.00", link.getPrice().toString());
		assertEquals("Unbranded", link.getBrand());
		assertEquals("Argos", link.getSeller());
		assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("3653351", link.getSku());
		assertEquals("Chad Valley Rectangular Paddling Pool -6ft-11in - 400 Litres", link.getName());
		assertEquals("18.00", link.getPrice().toString());
		assertEquals("Chad Valley", link.getBrand());
		assertEquals("Argos", link.getSeller());
		assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("8656793", link.getSku());
		assertEquals("SIM Free Huawei Mate 20 Pro 128GB Mobile - Twilight", link.getName());
		assertEquals("599.95", link.getPrice().toString());
		assertEquals("Huawei", link.getBrand());
		assertEquals("Argos", link.getSeller());
		assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_5() {
		Link link = site.test(Helpers.getHtmlPath(site, 5));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("9345151", link.getSku());
		assertEquals("RED5 Retro Mini Arcade Machine", link.getName());
		assertEquals("20.00", link.getPrice().toString());
		assertEquals("RED5", link.getBrand());
		assertEquals("Argos", link.getSeller());
		assertEquals("In-store pickup", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
