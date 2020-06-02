package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Lidl_UK_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("23068", competitor.getSku());
        assertEquals("Parkside 4Ah Battery", competitor.getName());
        assertEquals("24.99", competitor.getPrice().toString());
        assertEquals("Parkside", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("23141", competitor.getSku());
        assertEquals("Aquapur Dustpan & Brush Set", competitor.getName());
        assertEquals("2.49", competitor.getPrice().toString());
        assertEquals("Aquapur", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("23076", competitor.getSku());
        assertEquals("Powerfix Spirit Level Set", competitor.getName());
        assertEquals("7.99", competitor.getPrice().toString());
        assertEquals("Powerfix", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("23026", competitor.getSku());
        assertEquals("Ernesto Stainless Steel Colander", competitor.getName());
        assertEquals("2.99", competitor.getPrice().toString());
        assertEquals("Ernesto", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
