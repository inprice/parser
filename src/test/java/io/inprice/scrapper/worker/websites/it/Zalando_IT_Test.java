package io.inprice.scrapper.worker.websites.it;

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
public class Zalando_IT_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("F1451A005-K11", link.getSku());
        assertEquals("STUDDED GLOVES - Guanti", link.getName());
        assertEquals("103.99", link.getPrice().toString());
        assertEquals("Filippa K", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("M0Q21D0BE-Q11", link.getSku());
        assertEquals("SHEARED BANDEAU 2 PACK  - Top", link.getName());
        assertEquals("14.69", link.getPrice().toString());
        assertEquals("Missguided", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("APC31F002-G16", link.getSku());
        assertEquals("WATER LIGHT TINT - Tinta labbra", link.getName());
        assertEquals("8.99", link.getPrice().toString());
        assertEquals("A'PIEU", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("4SW51L0B2-F11", link.getSku());
        assertEquals("VINTAGE PENDANT PEAR - Collana", link.getName());
        assertEquals("79.99", link.getPrice().toString());
        assertEquals("Swarovski", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}