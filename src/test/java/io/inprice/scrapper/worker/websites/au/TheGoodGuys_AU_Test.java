package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TheGoodGuys_AU_Test {

    private final String SITE_NAME = "thegoodguys";
    private final String COUNTRY_CODE = "au";

    private final Website site = new TheGoodGuys(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("3542538", link.getSku());
        assertEquals("Fitbit Alta HR Black Small", link.getName());
        assertEquals("99.00", link.getPrice().toString());
        assertEquals("Fitbit", link.getBrand());
        assertEquals("TheGoodGuys", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("248407-01", link.getSku());
        assertEquals("Dyson V7 Cord-free Handstick", link.getName());
        assertEquals("399.00", link.getPrice().toString());
        assertEquals("Dyson", link.getBrand());
        assertEquals("TheGoodGuys", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("WH7560J3", link.getSku());
        assertEquals("Fisher & Paykel 7.5kg Front Load Washer", link.getName());
        assertEquals("598.00", link.getPrice().toString());
        assertEquals("Fisher & Paykel", link.getBrand());
        assertEquals("TheGoodGuys", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("3278984", link.getSku());
        assertEquals("Tom Tom Start 52 5\" GPS", link.getName());
        assertEquals("169.00", link.getPrice().toString());
        assertEquals("Tom Tom", link.getBrand());
        assertEquals("TheGoodGuys", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertNull(link.getSpecList());
    }

}
