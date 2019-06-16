package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Zalando_UK_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("AD121J0K8-I11", link.getSku());
        assertEquals("TREFOIL HOODIE - Hoodie", link.getName());
        assertEquals("41.90", link.getPrice().toString());
        assertEquals("adidas Originals", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("TO851L01L-F11", link.getSku());
        assertEquals("EARRINGS - Earrings - gold", link.getName());
        assertEquals("36.99", link.getPrice().toString());
        assertEquals("TomShot", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("M9121U0HC-K11", link.getSku());
        assertEquals("STAR - Classic coat", link.getName());
        assertEquals("69.99", link.getPrice().toString());
        assertEquals("Mango", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("RA251K00I-O11", link.getSku());
        assertEquals("Sunglasses", link.getName());
        assertEquals("109.99", link.getPrice().toString());
        assertEquals("Ray-Ban", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
