package io.inprice.scrapper.worker.websites.uk;

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
public class Currys_UK_Test {

    private final String SITE_NAME = "currys";
    private final String COUNTRY_CODE = "uk";

    private final Currys site = new Currys(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("182835", link.getSku());
        assertEquals("Series 7 7898CC Wet and Dry Foil Shaver - Silver", link.getName());
        assertEquals("199.00", link.getPrice().toString());
        assertEquals("BRAUN", link.getBrand());
        assertEquals("Currys", link.getSeller());
        assertEquals("FREE delivery available", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("196643", link.getSku());
        assertEquals("PerfectCare Elite GC9630/20 Steam Generator Iron - Navy & White", link.getName());
        assertEquals("235.00", link.getPrice().toString());
        assertEquals("PHILIPS", link.getBrand());
        assertEquals("Currys", link.getSeller());
        assertEquals("FREE delivery available", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("229085", link.getSku());
        assertEquals("CTL55W18 Tall Fridge - White", link.getName());
        assertEquals("199.00", link.getPrice().toString());
        assertEquals("ESSENTIALS", link.getBrand());
        assertEquals("Currys", link.getSeller());
        assertEquals("FREE delivery available", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("251311", link.getSku());
        assertEquals("by De'Longhi Infinissima EDG260.G Coffee Machine - Black & Grey", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("DOLCE GUSTO", link.getBrand());
        assertEquals("Currys", link.getSeller());
        assertEquals("FREE delivery available", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}