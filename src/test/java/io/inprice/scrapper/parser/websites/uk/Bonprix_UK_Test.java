package io.inprice.scrapper.parser.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_UK_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.Bonprix(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("936861-128-Black/White", competitor.getSku());
        assertEquals("Suit & Shirt & Tie by bpc bonprix collection | bonprix", competitor.getName());
        assertEquals("49.99", competitor.getPrice().toString());
        assertEquals("bpc bonprix collection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("3.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("960056-12-BlackPrint", competitor.getSku());
        assertEquals("Printed Wrap Tankini by bpc selection | bonprix", competitor.getName());
        assertEquals("37.99", competitor.getPrice().toString());
        assertEquals("bpc selection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("3.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("948669-12-BerryMarl", competitor.getSku());
        assertEquals("Soft Shell Parka by bpc bonprix collection | bonprix", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("bpc bonprix collection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("3.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("912263-1-GreyStripe", competitor.getSku());
        assertEquals("Tie & Pocket Square by bpc selection | bonprix", competitor.getName());
        assertEquals("12.99", competitor.getPrice().toString());
        assertEquals("bpc selection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("3.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
