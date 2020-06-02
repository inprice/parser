package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoolBlue_NL_Test {

    private final String SITE_NAME = "coolblue";
    private final String COUNTRY_CODE = "nl";

    private final CoolBlue site = new CoolBlue(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("447037", competitor.getSku());
        assertEquals("Stanley Air Kit", competitor.getName());
        assertEquals("109.00", competitor.getPrice().toString());
        assertEquals("Stanley", competitor.getBrand());
        assertEquals("CoolBlue", competitor.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("819196", competitor.getSku());
        assertEquals("GoPro HERO 7 Black", competitor.getName());
        assertEquals("349.00", competitor.getPrice().toString());
        assertEquals("GoPro", competitor.getBrand());
        assertEquals("CoolBlue", competitor.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("819780", competitor.getSku());
        assertEquals("TomTom GO Essential 5 Europa", competitor.getName());
        assertEquals("187.00", competitor.getPrice().toString());
        assertEquals("TomTom", competitor.getBrand());
        assertEquals("CoolBlue", competitor.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("601518", competitor.getSku());
        assertEquals("Bose SoundCompetitor Mini II Zwart", competitor.getName());
        assertEquals("139.00", competitor.getPrice().toString());
        assertEquals("Bose", competitor.getBrand());
        assertEquals("CoolBlue", competitor.getSeller());
        assertEquals("Voor 23.59 uur besteld, morgen gratis bezorgd", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
