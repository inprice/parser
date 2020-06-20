package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lidl_NL_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("19356", competitor.getSku());
        assertEquals("Heren sportshort", competitor.getName());
        assertEquals("5.99", competitor.getPrice().toString());
        assertEquals("Lidl.nl", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("19352", competitor.getSku());
        assertEquals("Activity-tracker", competitor.getName());
        assertEquals("39.99", competitor.getPrice().toString());
        assertEquals("Lidl.nl", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("19419", competitor.getSku());
        assertEquals("Jersey hoeslaken", competitor.getName());
        assertEquals("8.99", competitor.getPrice().toString());
        assertEquals("Lidl.nl", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("100257246", competitor.getSku());
        assertEquals("FLORABEST Relaxstoel", competitor.getName());
        assertEquals("39.99", competitor.getPrice().toString());
        assertEquals("", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("Eerste levering mogelijk vanaf 21-06-2019. Beschikbaarheid De verkoop van de artikelen uit " +
                "de folder start in de filialen op de aangegeven actiedag. O", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
