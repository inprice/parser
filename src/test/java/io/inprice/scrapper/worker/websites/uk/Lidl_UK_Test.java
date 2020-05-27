package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Lidl_UK_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("23068", link.getSku());
        assertEquals("Parkside 4Ah Battery", link.getName());
        assertEquals("24.99", link.getPrice().toString());
        assertEquals("Parkside", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("23141", link.getSku());
        assertEquals("Aquapur Dustpan & Brush Set", link.getName());
        assertEquals("2.49", link.getPrice().toString());
        assertEquals("Aquapur", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("23076", link.getSku());
        assertEquals("Powerfix Spirit Level Set", link.getName());
        assertEquals("7.99", link.getPrice().toString());
        assertEquals("Powerfix", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("23026", link.getSku());
        assertEquals("Ernesto Stainless Steel Colander", link.getName());
        assertEquals("2.99", link.getPrice().toString());
        assertEquals("Ernesto", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
