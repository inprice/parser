package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Walmart_US_Test {

    private final String SITE_NAME = "walmart";
    private final String COUNTRY_CODE = "us";

    private final Walmart site = new Walmart(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("55508481", competitor.getSku());
        assertEquals("Ozark Trail 12x12 Slant Leg Canopy", competitor.getName());
        assertEquals("49.20", competitor.getPrice().toString());
        assertEquals("Ozark Trail", competitor.getBrand());
        assertEquals("Walmart", competitor.getSeller());
        assertEquals("Free 2-day delivery", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("911905348", competitor.getSku());
        assertEquals("Hilasal Palm Island Fiber-Reaction Printed Beach Towel - 30 x 60 inches 12058", competitor.getName());
        assertEquals("9.95", competitor.getPrice().toString());
        assertEquals("Hilasal USA®", competitor.getBrand());
        assertEquals("American Living Online", competitor.getSeller());
        assertEquals("Free delivery", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("257080810", competitor.getSku());
        assertEquals("POP TV: Stranger Things- 6\" Big Demogorgon", competitor.getName());
        assertEquals("13.76", competitor.getPrice().toString());
        assertEquals("Funko", competitor.getBrand());
        assertEquals("Calendars LLC", competitor.getSeller());
        assertEquals("Free delivery", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("568773858", competitor.getSku());
        assertEquals("Theragun liv Handheld Compact Percussive Therapy Device, Portable Muscle Massager", competitor.getName());
        assertEquals("299.00", competitor.getPrice().toString());
        assertEquals("Theragun", competitor.getBrand());
        assertEquals("Walmart", competitor.getSeller());
        assertEquals("Free delivery", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
