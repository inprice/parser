package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_CA_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "ca";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("303117084799", competitor.getSku());
        assertEquals("Beats Studio3 Wireless Over-Ear Headphones - NBA Collection - Raptors White", competitor.getName());
        assertEquals("214.99", competitor.getPrice().toString());
        assertEquals("Beats by Dr. Dre", competitor.getBrand());
        assertEquals("best-tech-dealz", competitor.getSeller());
        assertEquals("May not ship to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("142652956613", competitor.getSku());
        assertEquals("5 x 1 oz 2019 Silver Maple Leaf Coin RCM - Royal Canadian Mint", competitor.getName());
        assertEquals("121.23", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("globalbullion", competitor.getSeller());
        assertEquals("May not ship to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("222999332993", competitor.getSku());
        assertEquals("LivingBasics 1000W 16-in-1 Electric Pressure Cooker Brushed Stainless, 5-Quart", competitor.getName());
        assertEquals("61.99", competitor.getPrice().toString());
        assertEquals("LivingBasics", competitor.getBrand());
        assertEquals("saveonmany", competitor.getSeller());
        assertEquals("May not ship to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("123784412238", competitor.getSku());
        assertEquals("Lenovo ThinkPad X201 12.1\" Laptop Intel i5-540M 2.53GHz 4GB 320GB Window 10 Home", competitor.getName());
        assertEquals("189.99", competitor.getPrice().toString());
        assertEquals("Lenovo", competitor.getBrand());
        assertEquals("refurbio", competitor.getSeller());
        assertEquals("Does not ship to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
