package io.inprice.scrapper.worker.websites.tr;

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
    private final String COUNTRY_CODE = "tr";

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
        assertEquals("HJ162ZM/A", link.getSku());
        assertEquals("SteelSeries Nimbus Kablosuz Oyun Kumandası", link.getName());
        assertEquals("549.00", link.getPrice().toString());
        assertEquals("SteelSeries", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MPXT2TU/A", link.getSku());
        assertEquals("13 inç MacBook Pro - Uzay Grisi", link.getName());
        assertEquals("11399.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MQ6H2TU/A", link.getSku());
        assertEquals("iPhone 8 64 GB Gümüş", link.getName());
        assertEquals("5799.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MU662TU/A", link.getSku());
        assertEquals("Apple Watch Series 4 GPS, 40 mm Uzay Grisi Alüminyum Kasa ve Siyah Spor Kordon", link.getName());
        assertEquals("3099.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertNull(link.getSpecList());
    }

}
