package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class Apple {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "de";

    private final Website site;

    public Apple() {
        Link link = new Link();
        link.setUrl(String.format("https://www.apple.com/%s/shop/", COUNTRY_CODE));
        site = new io.inprice.scrapper.worker.websites.xx.Apple(link);
    }

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MR912FD/A", link.getSku());
        assertEquals("Apple TV HD 32 GB", link.getName());
        assertEquals("159.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Expressversand. Kostenlos", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MRT42D/A", link.getSku());
        assertEquals("21,5\" iMac mit Retina 4K Display", link.getName());
        assertEquals("1699.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Standardversand. Kostenlos", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MTFL2FD/A", link.getSku());
        assertEquals("12,9\" iPad Pro Wi‑Fi 256 GB – Space Grau", link.getName());
        assertEquals("1269.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Expressversand. Kostenlos", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MRY72ZD/A", link.getSku());
        assertEquals("iPhone XR 64 GB Gelb", link.getName());
        assertEquals("849.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Expressversand. Kostenlos", link.getShipment());
        assertNull(link.getSpecList());
    }

}
