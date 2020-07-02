package io.inprice.parser.websites.us;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_US_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "us";

    private final Website site = new io.inprice.parser.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("192644184854", competitor.getSku());
        assertEquals("FXR Mens Black/Lime Snowmobile Helix Jacket Snocross", competitor.getName());
        assertEquals("136.00", competitor.getPrice().toString());
        assertEquals("FXR", competitor.getBrand());
        assertEquals("mxgear", competitor.getSeller());
        assertEquals("$78.45 USPS Priority Mail International", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("252786445351", competitor.getSku());
        assertEquals("Fashion 16GB-64GB USB Flash Drive Metal Crystal Heart Design Flash Memory Stick", competitor.getName());
        assertEquals("7.99", competitor.getPrice().toString());
        assertEquals("Kootion", competitor.getBrand());
        assertEquals("koohome", competitor.getSeller());
        assertEquals("Does not ship to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("253288730730", competitor.getSku());
        assertEquals("60 Colors Nail Art Tips Wraps Transfer Foil A* US SELLER * BUY2GET1FREE", competitor.getName());
        assertEquals("0.99", competitor.getPrice().toString());
        assertEquals("Unbranded", competitor.getBrand());
        assertEquals("gift4her2016", competitor.getSeller());
        assertEquals("May not ship to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("223510923884", competitor.getSku());
        assertEquals("Women's Beachwear Swimwear Bikini Beach Wear Cover Up Tassel Ladies Summer Dress", competitor.getName());
        assertEquals("8.07", competitor.getPrice().toString());
        assertEquals("Unbranded", competitor.getBrand());
        assertEquals("gangti_66", competitor.getSeller());
        assertEquals("FREE Economy International Shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
