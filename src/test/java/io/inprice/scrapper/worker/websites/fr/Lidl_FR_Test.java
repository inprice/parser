package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lidl_FR_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("32120", link.getSku());
        assertEquals("Mini jardin d'intérieur", link.getName());
        assertEquals("7.99", link.getPrice().toString());
        assertEquals("lidl.fr", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("32026", link.getSku());
        assertEquals("Spray solaire transparent", link.getName());
        assertEquals("4.99", link.getPrice().toString());
        assertEquals("lidl.fr", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("32047", link.getSku());
        assertEquals("Perceuse d’établi", link.getName());
        assertEquals("79.99", link.getPrice().toString());
        assertEquals("lidl.fr", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("21782", link.getSku());
        assertEquals("Gallia Calisma 2", link.getName());
        assertEquals("13.19", link.getPrice().toString());
        assertEquals("lidl.fr", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
