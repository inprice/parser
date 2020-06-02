package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lidl_DE_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("000000000100234276", competitor.getSku());
        assertEquals("Hoover Waschmaschine HL4 1472D3/1-S", competitor.getName());
        assertEquals("319.00", competitor.getPrice().toString());
        assertEquals("Hoover", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Innerhalb von 5 Werktagen", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("000000000100239698", competitor.getSku());
        assertEquals("DENVER Bluetooth Smartwatch SW-500", competitor.getName());
        assertEquals("99.99", competitor.getPrice().toString());
        assertEquals("DENVER", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Innerhalb von 3 Werktagen", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("000000000100203237", competitor.getSku());
        assertEquals("BIG Spielfahrzeug Power Worker Radlader", competitor.getName());
        assertEquals("15.99", competitor.getPrice().toString());
        assertEquals("BIG", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Innerhalb von 3 Werktagen", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("000000000100031233", competitor.getSku());
        assertEquals("Christopeit Total Exerciser TE 1", competitor.getName());
        assertEquals("115.00", competitor.getPrice().toString());
        assertEquals("Christopeit", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Innerhalb von 3 Werktagen", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
