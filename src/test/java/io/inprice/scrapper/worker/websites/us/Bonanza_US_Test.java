package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Bonanza_US_Test {

    private final String SITE_NAME = "bonanza";
    private final String COUNTRY_CODE = "us";

    private final Bonanza site = new Bonanza(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("727609404", link.getSku());
        assertEquals("Men's Necklace Set - Jesus Face & Angel Baby Micro Pendant w/ An 18\"+2\" Chain", link.getName());
        assertEquals("9.79", link.getPrice().toString());
        assertEquals("Aventura", link.getBrand());
        assertEquals("Aventura Jewelry", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("494281674", link.getSku());
        assertEquals("Martha Blue Leather Purse Clutch With Silver Hardware", link.getName());
        assertEquals("27.00", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("Kate Bissett New York", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("728313952", link.getSku());
        assertEquals("Plug in Modern Crystal Chandelier Swag Pendant Light with Clear 16.4' Cord and I", link.getName());
        assertEquals("43.79", link.getPrice().toString());
        assertEquals("Creatgeek", link.getBrand());
        assertEquals("NicholasP991's booth", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("725584787", link.getSku());
        assertEquals("Wood Towel Rack Wall 22” Vtg Antique Painted Starburst Design Victorian", link.getName());
        assertEquals("148.49", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("PanAnnAmericana's World Port", link.getSeller());
        assertEquals("NA", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}