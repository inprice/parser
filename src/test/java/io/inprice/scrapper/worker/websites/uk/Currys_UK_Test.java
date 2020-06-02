package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Currys_UK_Test {

    private final String SITE_NAME = "currys";
    private final String COUNTRY_CODE = "uk";

    private final Currys site = new Currys(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("182835", competitor.getSku());
        assertEquals("Series 7 7898CC Wet and Dry Foil Shaver - Silver", competitor.getName());
        assertEquals("199.00", competitor.getPrice().toString());
        assertEquals("BRAUN", competitor.getBrand());
        assertEquals("Currys", competitor.getSeller());
        assertEquals("FREE delivery available", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("196643", competitor.getSku());
        assertEquals("PerfectCare Elite GC9630/20 Steam Generator Iron - Navy & White", competitor.getName());
        assertEquals("235.00", competitor.getPrice().toString());
        assertEquals("PHILIPS", competitor.getBrand());
        assertEquals("Currys", competitor.getSeller());
        assertEquals("FREE delivery available", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("229085", competitor.getSku());
        assertEquals("CTL55W18 Tall Fridge - White", competitor.getName());
        assertEquals("199.00", competitor.getPrice().toString());
        assertEquals("ESSENTIALS", competitor.getBrand());
        assertEquals("Currys", competitor.getSeller());
        assertEquals("FREE delivery available", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("251311", competitor.getSku());
        assertEquals("by De'Longhi Infinissima EDG260.G Coffee Machine - Black & Grey", competitor.getName());
        assertEquals("49.99", competitor.getPrice().toString());
        assertEquals("DOLCE GUSTO", competitor.getBrand());
        assertEquals("Currys", competitor.getSeller());
        assertEquals("FREE delivery available", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
