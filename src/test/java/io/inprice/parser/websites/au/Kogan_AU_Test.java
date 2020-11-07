package io.inprice.parser.websites.au;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Kogan_AU_Test {

    private final String SITE_NAME = "kogan";
    private final String COUNTRY_CODE = "au";

    private final Website site = new Kogan();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("KAODPDLKLBA", link.getSku());
        assertEquals("Kogan Outdoor Padlock Lockbox", link.getName());
        assertEquals("19.00", link.getPrice().toString());
        assertEquals("Kogan", link.getBrand());
        assertEquals("Kogan", link.getSeller());
        assertEquals("Free Shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("KAWWASHT10A", link.getSku());
        assertEquals("Kogan 10KG Top Load Washing Machine", link.getName());
        assertEquals("299.00", link.getPrice().toString());
        assertEquals("Kogan", link.getBrand());
        assertEquals("Kogan", link.getSeller());
        assertEquals(Consts.Words.NOT_AVAILABLE, link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("CT18VIMPWRB", link.getSku());
        assertEquals("Certa PowerPlus 18V Cordless Impact Driver (Skin Only)", link.getName());
        assertEquals("45.00", link.getPrice().toString());
        assertEquals("Certa", link.getBrand());
        assertEquals("Kogan", link.getSeller());
        assertEquals("Free Shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("MLB-PLTL21BMRA2C2", link.getSku());
        assertEquals("Baumr-AG 65CC Long Reach Pole Chainsaw Hedge Trimmer Pruner Chain Saw Tree Multi Tool", link.getName());
        assertEquals("239.00", link.getPrice().toString());
        assertEquals("Baumr-AG", link.getBrand());
        assertEquals("Kogan", link.getSeller());
        assertEquals("Free Shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}