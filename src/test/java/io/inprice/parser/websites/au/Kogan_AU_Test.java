package io.inprice.parser.websites.au;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Kogan_AU_Test {

    private final String SITE_NAME = "kogan";
    private final String COUNTRY_CODE = "au";

    private final Website site = new Kogan(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("KAODPDLKLBA", competitor.getSku());
        assertEquals("Kogan Outdoor Padlock Lockbox", competitor.getName());
        assertEquals("19.00", competitor.getPrice().toString());
        assertEquals("Kogan", competitor.getBrand());
        assertEquals("Kogan", competitor.getSeller());
        assertEquals("Free Shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("KAWWASHT10A", competitor.getSku());
        assertEquals("Kogan 10KG Top Load Washing Machine", competitor.getName());
        assertEquals("299.00", competitor.getPrice().toString());
        assertEquals("Kogan", competitor.getBrand());
        assertEquals("Kogan", competitor.getSeller());
        assertEquals("NA", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("CT18VIMPWRB", competitor.getSku());
        assertEquals("Certa PowerPlus 18V Cordless Impact Driver (Skin Only)", competitor.getName());
        assertEquals("45.00", competitor.getPrice().toString());
        assertEquals("Certa", competitor.getBrand());
        assertEquals("Kogan", competitor.getSeller());
        assertEquals("Free Shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("MLB-PLTL21BMRA2C2", competitor.getSku());
        assertEquals("Baumr-AG 65CC Long Reach Pole Chainsaw Hedge Trimmer Pruner Chain Saw Tree Multi Tool", competitor.getName());
        assertEquals("239.00", competitor.getPrice().toString());
        assertEquals("Baumr-AG", competitor.getBrand());
        assertEquals("Kogan", competitor.getSeller());
        assertEquals("Free Shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
