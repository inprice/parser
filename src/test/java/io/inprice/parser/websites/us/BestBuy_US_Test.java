package io.inprice.parser.websites.us;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BestBuy_US_Test {

    private final String SITE_NAME = "bestbuy";
    private final String COUNTRY_CODE = "us";

    private final BestBuy site = new BestBuy(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6331929", competitor.getSku());
        assertEquals("Dyson - V11 Torque Drive Cord-Free Vacuum - Blue/Nickel", competitor.getName());
        assertEquals("699.99", competitor.getPrice().toString());
        assertEquals("Dyson", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6280544", competitor.getSku());
        assertEquals("Sony - WH-1000XM3 Wireless Noise Canceling Over-the-Ear Headphones with Google Assistant - Black", competitor.getName());
        assertEquals("299.99", competitor.getPrice().toString());
        assertEquals("Sony", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6329758", competitor.getSku());
        assertEquals("CyberPowerPC - Gaming Desktop - AMD Ryzen 7 2700 - 16GB Memory - AMD RX 580 8GB - 2TB Hard Drive + 240GB Solid State Drive - White", competitor.getName());
        assertEquals("849.99", competitor.getPrice().toString());
        assertEquals("CyberPowerPC", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6332011", competitor.getSku());
        assertEquals("Dyson - TP01 Pure Cool Tower 172 Sq. Ft. Air Purifier and Fan - Iron/Blue", competitor.getName());
        assertEquals("299.99", competitor.getPrice().toString());
        assertEquals("Dyson", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
