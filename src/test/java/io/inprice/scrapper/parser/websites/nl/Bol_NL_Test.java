package io.inprice.scrapper.parser.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bol_NL_Test {

    private final String SITE_NAME = "bol";
    private final String COUNTRY_CODE = "nl";

    private final Bol site = new Bol(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9200000080451630", competitor.getSku());
        assertEquals("Eagle Creek Pack-It Specter Tech Cube Set XS/S/M Black", competitor.getName());
        assertEquals("40.99", competitor.getPrice().toString());
        assertEquals("Eagle Creek", competitor.getBrand());
        assertEquals("bol.com", competitor.getSeller());
        assertEquals("Gratis verzending", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9200000108307626", competitor.getSku());
        assertEquals("Dyson Pure Cool Me - Luchtreiniger", competitor.getName());
        assertEquals("349.00", competitor.getPrice().toString());
        assertEquals("Dyson", competitor.getBrand());
        assertEquals("bol.com", competitor.getSeller());
        assertEquals("Gratis verzending", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9200000106883561", competitor.getSku());
        assertEquals("Samsung Galaxy A50 - 128GB - Zwart", competitor.getName());
        assertEquals("279.00", competitor.getPrice().toString());
        assertEquals("Samsung", competitor.getBrand());
        assertEquals("bol.com", competitor.getSeller());
        assertEquals("Gratis verzending", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9200000099723478", competitor.getSku());
        assertEquals("Andrélon Iedere Dag Shampoo - 3 x 300 ml - Voordeelverpakking", competitor.getName());
        assertEquals("5.09", competitor.getPrice().toString());
        assertEquals("Andrélon", competitor.getBrand());
        assertEquals("bol.com", competitor.getSeller());
        assertEquals("Gratis verzending vanaf 20 euro", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
