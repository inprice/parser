package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_UK_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("936861-128-Black/White", link.getSku());
        assertEquals("Suit & Shirt & Tie by bpc bonprix collection | bonprix", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("960056-12-BlackPrint", link.getSku());
        assertEquals("Printed Wrap Tankini by bpc selection | bonprix", link.getName());
        assertEquals("37.99", link.getPrice().toString());
        assertEquals("bpc selection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("948669-12-BerryMarl", link.getSku());
        assertEquals("Soft Shell Parka by bpc bonprix collection | bonprix", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("912263-1-GreyStripe", link.getSku());
        assertEquals("Tie & Pocket Square by bpc selection | bonprix", link.getName());
        assertEquals("12.99", link.getPrice().toString());
        assertEquals("bpc selection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
