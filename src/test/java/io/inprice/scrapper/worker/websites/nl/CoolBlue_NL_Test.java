package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoolBlue_NL_Test {

    private final String SITE_NAME = "coolblue";
    private final String COUNTRY_CODE = "nl";

    private final CoolBlue site = new CoolBlue(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("447037", link.getSku());
        assertEquals("Stanley Air Kit", link.getName());
        assertEquals("109.00", link.getPrice().toString());
        assertEquals("Stanley", link.getBrand());
        assertEquals("CoolBlue", link.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("819196", link.getSku());
        assertEquals("GoPro HERO 7 Black", link.getName());
        assertEquals("349.00", link.getPrice().toString());
        assertEquals("GoPro", link.getBrand());
        assertEquals("CoolBlue", link.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("819780", link.getSku());
        assertEquals("TomTom GO Essential 5 Europa", link.getName());
        assertEquals("187.00", link.getPrice().toString());
        assertEquals("TomTom", link.getBrand());
        assertEquals("CoolBlue", link.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("601518", link.getSku());
        assertEquals("Bose SoundLink Mini II Zwart", link.getName());
        assertEquals("139.00", link.getPrice().toString());
        assertEquals("Bose", link.getBrand());
        assertEquals("CoolBlue", link.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
