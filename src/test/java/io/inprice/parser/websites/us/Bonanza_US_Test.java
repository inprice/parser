package io.inprice.parser.websites.us;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonanza_US_Test {

    private final String SITE_NAME = "bonanza";
    private final String COUNTRY_CODE = "us";

    private final Bonanza site = new Bonanza(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("727609404", competitor.getSku());
        assertEquals("Men's Necklace Set - Jesus Face & Angel Baby Micro Pendant w/ An 18\"+2\" Chain", competitor.getName());
        assertEquals("9.79", competitor.getPrice().toString());
        assertEquals("Aventura", competitor.getBrand());
        assertEquals("Aventura Jewelry", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("494281674", competitor.getSku());
        assertEquals("Martha Blue Leather Purse Clutch With Silver Hardware", competitor.getName());
        assertEquals("27.00", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("Kate Bissett New York", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("728313952", competitor.getSku());
        assertEquals("Plug in Modern Crystal Chandelier Swag Pendant Light with Clear 16.4' Cord and I", competitor.getName());
        assertEquals("43.79", competitor.getPrice().toString());
        assertEquals("Creatgeek", competitor.getBrand());
        assertEquals("NicholasP991's booth", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("725584787", competitor.getSku());
        assertEquals("Wood Towel Rack Wall 22” Vtg Antique Painted Starburst Design Victorian", competitor.getName());
        assertEquals("148.49", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("PanAnnAmericana's World Port", competitor.getSeller());
        assertEquals("NA", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
