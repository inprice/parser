package io.inprice.scrapper.parser.websites.au;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BigW_AU_Test {

    private final String SITE_NAME = "bigw";
    private final String COUNTRY_CODE = "au";

    private final Website site = new io.inprice.scrapper.parser.websites.au.BigW(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("827030", competitor.getSku());
        assertEquals("Repco Blade 20 Boys 50cm Mountain Bike", competitor.getName());
        assertEquals("99.00", competitor.getPrice().toString());
        assertEquals("Repco", competitor.getBrand());
        assertEquals("Big W", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("33770", competitor.getSku());
        assertEquals("Laser Karaoke LED Microphone - Pink", competitor.getName());
        assertEquals("39.00", competitor.getPrice().toString());
        assertEquals("Laser", competitor.getBrand());
        assertEquals("Big W", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("820960", competitor.getSku());
        assertEquals("NERF Mega Megalodon including 20 Mega Darts", competitor.getName());
        assertEquals("49.00", competitor.getPrice().toString());
        assertEquals("Nerf", competitor.getBrand());
        assertEquals("Big W", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("821107", competitor.getSku());
        assertEquals("Barbie Ultra Plus Folding Booster Seat", competitor.getName());
        assertEquals("129.00", competitor.getPrice().toString());
        assertEquals("The First Years", competitor.getBrand());
        assertEquals("Big W", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
