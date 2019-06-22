package io.inprice.scrapper.worker.websites.au;

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
public class BigW_AU_Test {

    private final String SITE_NAME = "bigw";
    private final String COUNTRY_CODE = "au";

    private final Website site = new io.inprice.scrapper.worker.websites.au.BigW(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("827030", link.getSku());
        assertEquals("Repco Blade 20 Boys 50cm Mountain Bike", link.getName());
        assertEquals("99.00", link.getPrice().toString());
        assertEquals("Repco", link.getBrand());
        assertEquals("Big W", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("33770", link.getSku());
        assertEquals("Laser Karaoke LED Microphone - Pink", link.getName());
        assertEquals("39.00", link.getPrice().toString());
        assertEquals("Laser", link.getBrand());
        assertEquals("Big W", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("820960", link.getSku());
        assertEquals("NERF Mega Megalodon including 20 Mega Darts", link.getName());
        assertEquals("49.00", link.getPrice().toString());
        assertEquals("Nerf", link.getBrand());
        assertEquals("Big W", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("821107", link.getSku());
        assertEquals("Barbie Ultra Plus Folding Booster Seat", link.getName());
        assertEquals("129.00", link.getPrice().toString());
        assertEquals("The First Years", link.getBrand());
        assertEquals("Big W", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
