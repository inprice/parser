package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Euronics_IT_Test {

    private final String SITE_NAME = "euronics";
    private final String COUNTRY_CODE = "it";

    private final Euronics site = new Euronics(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.UNAVAILABLE, link.getStatus());
        assertEquals("eProd182001118", link.getSku());
        assertEquals("ASUS - CERBERUS V2/RED - Nero/Rosso", link.getName());
        assertEquals("0.00", link.getPrice().toString());
        assertEquals("ASUS", link.getBrand());
        assertEquals("Euronics", link.getSeller());
        assertEquals("NA", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("eProd182001461", link.getSku());
        assertEquals("OLIMPIA SPLENDID - Peler 4E - Bianco", link.getName());
        assertEquals("79.99", link.getPrice().toString());
        assertEquals("OLIMPIA SPLENDID", link.getBrand());
        assertEquals("Euronics", link.getSeller());
        assertEquals("Consegna €11", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("eProd192005354", link.getSku());
        assertEquals("SAMSUNG - RB41R7719S9/EF - metal inox", link.getName());
        assertEquals("899.00", link.getPrice().toString());
        assertEquals("SAMSUNG", link.getBrand());
        assertEquals("Euronics", link.getSeller());
        assertEquals("Consegna al piano €77", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("eProd192003278", link.getSku());
        assertEquals("XIAOMI - Redmi Note 7 4+64 - Blu", link.getName());
        assertEquals("199.00", link.getPrice().toString());
        assertEquals("XIAOMI", link.getBrand());
        assertEquals("Euronics", link.getSeller());
        assertEquals("Consegna €11", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
