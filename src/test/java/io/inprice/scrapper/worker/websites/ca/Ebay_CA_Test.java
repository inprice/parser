package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Ebay_CA_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "ca";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("303117084799", link.getSku());
        assertEquals("Beats Studio3 Wireless Over-Ear Headphones - NBA Collection - Raptors White", link.getName());
        assertEquals("214.99", link.getPrice().toString());
        assertEquals("Beats by Dr. Dre", link.getBrand());
        assertEquals("best-tech-dealz", link.getSeller());
        assertEquals("May not ship to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("142652956613", link.getSku());
        assertEquals("5 x 1 oz 2019 Silver Maple Leaf Coin RCM - Royal Canadian Mint", link.getName());
        assertEquals("121.23", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("globalbullion", link.getSeller());
        assertEquals("May not ship to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("222999332993", link.getSku());
        assertEquals("LivingBasics® 1000W 16-in-1 Electric Pressure Cooker Brushed Stainless, 5-Quart", link.getName());
        assertEquals("61.99", link.getPrice().toString());
        assertEquals("LivingBasics", link.getBrand());
        assertEquals("saveonmany", link.getSeller());
        assertEquals("May not ship to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("123784412238", link.getSku());
        assertEquals("Lenovo ThinkPad X201 12.1\" Laptop Intel i5-540M 2.53GHz 4GB 320GB Window 10 Home", link.getName());
        assertEquals("189.99", link.getPrice().toString());
        assertEquals("Lenovo", link.getBrand());
        assertEquals("refurbio", link.getSeller());
        assertEquals("Does not ship to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}