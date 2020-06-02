package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class TheGoodGuys_AU_Test {

    private final String SITE_NAME = "thegoodguys";
    private final String COUNTRY_CODE = "au";

    private final Website site = new TheGoodGuys(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3542538", competitor.getSku());
        assertEquals("Fitbit Alta HR Black Small", competitor.getName());
        assertEquals("99.00", competitor.getPrice().toString());
        assertEquals("Fitbit", competitor.getBrand());
        assertEquals("TheGoodGuys", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("248407-01", competitor.getSku());
        assertEquals("Dyson V7 Cord-free Handstick", competitor.getName());
        assertEquals("399.00", competitor.getPrice().toString());
        assertEquals("Dyson", competitor.getBrand());
        assertEquals("TheGoodGuys", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("WH7560J3", competitor.getSku());
        assertEquals("Fisher & Paykel 7.5kg Front Load Washer", competitor.getName());
        assertEquals("598.00", competitor.getPrice().toString());
        assertEquals("Fisher & Paykel", competitor.getBrand());
        assertEquals("TheGoodGuys", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3278984", competitor.getSku());
        assertEquals("Tom Tom Start 52 5\" GPS", competitor.getName());
        assertEquals("169.00", competitor.getPrice().toString());
        assertEquals("Tom Tom", competitor.getBrand());
        assertEquals("TheGoodGuys", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

}
