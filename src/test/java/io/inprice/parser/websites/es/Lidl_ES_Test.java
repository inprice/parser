package io.inprice.parser.websites.es;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lidl_ES_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.parser.websites.xx.Lidl(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("302667", competitor.getSku());
        assertEquals("Plancha de vapor 240 V", competitor.getName());
        assertEquals("24.99", competitor.getPrice().toString());
        assertEquals("Silvercrest", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Envío entre 1 y 3 días laborables.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("311062", competitor.getSku());
        assertEquals("Isla hinchable piscina", competitor.getName());
        assertEquals("74.99", competitor.getPrice().toString());
        assertEquals("Crivit", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Envío entre 1 y 3 días laborables.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("54661", competitor.getSku());
        assertEquals("'Livergy' Bañador hombre", competitor.getName());
        assertEquals("4.99", competitor.getPrice().toString());
        assertEquals("lidl.es", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("54493", competitor.getSku());
        assertEquals("Crosandra", competitor.getName());
        assertEquals("3.99", competitor.getPrice().toString());
        assertEquals("lidl.es", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
