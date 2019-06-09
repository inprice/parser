package io.inprice.scrapper.worker.websites.au;

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
    private final String COUNTRY_CODE = "au";

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
        assertEquals("MTXP2X/A", link.getSku());
        assertEquals("11-inch iPad Pro Wi-Fi 64GB — Silver", link.getName());
        assertEquals("1229.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MRE92X/A", link.getSku());
        assertEquals("13-inch MacBook Air — Space Grey", link.getName());
        assertEquals("2149.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MQ7F2X/A", link.getSku());
        assertEquals("iPhone 8 256GB Space Grey", link.getName());
        assertEquals("1229.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("MQHV2X/A", link.getSku());
        assertEquals("HomePod — White", link.getName());
        assertEquals("469.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertNull(link.getSpecList());
    }

}
