package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_US_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "us";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("192644184854", link.getSku());
        assertEquals("FXR Mens Black/Lime Snowmobile Helix Jacket Snocross", link.getName());
        assertEquals("136.00", link.getPrice().toString());
        assertEquals("FXR", link.getBrand());
        assertEquals("mxgear", link.getSeller());
        assertEquals("$78.45 USPS Priority Mail International", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("252786445351", link.getSku());
        assertEquals("Fashion 16GB-64GB USB Flash Drive Metal Crystal Heart Design Flash Memory Stick", link.getName());
        assertEquals("7.99", link.getPrice().toString());
        assertEquals("Kootion", link.getBrand());
        assertEquals("koohome", link.getSeller());
        assertEquals("Does not ship to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("253288730730", link.getSku());
        assertEquals("60 Colors Nail Art Tips Wraps Transfer Foil A* US SELLER * BUY2GET1FREE", link.getName());
        assertEquals("0.99", link.getPrice().toString());
        assertEquals("Unbranded", link.getBrand());
        assertEquals("gift4her2016", link.getSeller());
        assertEquals("May not ship to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("223510923884", link.getSku());
        assertEquals("Women's Beachwear Swimwear Bikini Beach Wear Cover Up Tassel Ladies Summer Dress", link.getName());
        assertEquals("8.07", link.getPrice().toString());
        assertEquals("Unbranded", link.getBrand());
        assertEquals("gangti_66", link.getSeller());
        assertEquals("FREE Economy International Shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
