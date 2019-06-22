package io.inprice.scrapper.worker.websites.es;

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
public class Lidl_ES_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("302667", link.getSku());
        assertEquals("Plancha de vapor 240 V", link.getName());
        assertEquals("24.99", link.getPrice().toString());
        assertEquals("Silvercrest", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Envío entre 1 y 3 días laborables.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("311062", link.getSku());
        assertEquals("Isla hinchable piscina", link.getName());
        assertEquals("74.99", link.getPrice().toString());
        assertEquals("Crivit", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Envío entre 1 y 3 días laborables.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("54661", link.getSku());
        assertEquals("'Livergy®' Bañador hombre", link.getName());
        assertEquals("4.99", link.getPrice().toString());
        assertEquals("lidl.es", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("54493", link.getSku());
        assertEquals("Crosandra", link.getName());
        assertEquals("3.99", link.getPrice().toString());
        assertEquals("lidl.es", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}