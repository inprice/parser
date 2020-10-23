package io.inprice.parser.websites.us;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BestBuy_US_Test {

    private final String SITE_NAME = "bestbuy";
    private final String COUNTRY_CODE = "us";

    private final BestBuy site = new BestBuy();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("6331929", link.getSku());
        assertEquals("Dyson - V11 Torque Drive Cord-Free Vacuum - Blue/Nickel", link.getName());
        assertEquals("699.99", link.getPrice().toString());
        assertEquals("Dyson", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("6280544", link.getSku());
        assertEquals("Sony - WH-1000XM3 Wireless Noise Canceling Over-the-Ear Headphones with Google Assistant - Black", link.getName());
        assertEquals("299.99", link.getPrice().toString());
        assertEquals("Sony", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("6329758", link.getSku());
        assertEquals("CyberPowerPC - Gaming Desktop - AMD Ryzen 7 2700 - 16GB Memory - AMD RX 580 8GB - 2TB Hard Drive + 240GB Solid State Drive - White", link.getName());
        assertEquals("849.99", link.getPrice().toString());
        assertEquals("CyberPowerPC", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("6332011", link.getSku());
        assertEquals("Dyson - TP01 Pure Cool Tower 172 Sq. Ft. Air Purifier and Fan - Iron/Blue", link.getName());
        assertEquals("299.99", link.getPrice().toString());
        assertEquals("Dyson", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
