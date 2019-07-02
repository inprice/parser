package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.it.EPrice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Bol_NL_Test {

    private final String SITE_NAME = "bol";
    private final String COUNTRY_CODE = "nl";

    private final Bol site = new Bol(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("9200000080451630", link.getSku());
        assertEquals("Eagle Creek Pack-It Specter Tech Cube Set XS/S/M Black", link.getName());
        assertEquals("40.99", link.getPrice().toString());
        assertEquals("Eagle Creek", link.getBrand());
        assertEquals("bol.com", link.getSeller());
        assertEquals("Gratis verzending", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("9200000108307626", link.getSku());
        assertEquals("Dyson Pure Cool Me - Luchtreiniger", link.getName());
        assertEquals("349.00", link.getPrice().toString());
        assertEquals("Dyson", link.getBrand());
        assertEquals("bol.com", link.getSeller());
        assertEquals("Gratis verzending", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("9200000106883561", link.getSku());
        assertEquals("Samsung Galaxy A50 - 128GB - Zwart", link.getName());
        assertEquals("279.00", link.getPrice().toString());
        assertEquals("Samsung", link.getBrand());
        assertEquals("bol.com", link.getSeller());
        assertEquals("Gratis verzending", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("9200000099723478", link.getSku());
        assertEquals("Andrélon Iedere Dag Shampoo - 3 x 300 ml - Voordeelverpakking", link.getName());
        assertEquals("5.09", link.getPrice().toString());
        assertEquals("Andrélon", link.getBrand());
        assertEquals("bol.com", link.getSeller());
        assertEquals("Gratis verzending vanaf 20 euro", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
