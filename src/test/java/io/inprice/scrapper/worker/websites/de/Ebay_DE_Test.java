package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_DE_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("370503654868", link.getSku());
        assertEquals("SURPLUS DIVISION CARGO SHORT 7FARBEN XS-7XL U.S. Army Bermuda Rider Shorts Walk", link.getName());
        assertEquals("29.90", link.getPrice().toString());
        assertEquals("Surplus Raw Vintage", link.getBrand());
        assertEquals("urbandreamz_ltd", link.getSeller());
        assertEquals("EUR 34,90 Standardversand", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("254158224125", link.getSku());
        assertEquals("Luxus Gold Panzerkette Armband Set Herrenkette Edelstahl 18 Karat vergoldet 60cm", link.getName());
        assertEquals("32.39", link.getPrice().toString());
        assertEquals("Beyalyjwls", link.getBrand());
        assertEquals("beyalyjwls", link.getSeller());
        assertEquals("Möglicherweise kein Versand nach Türkei", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("143052381051", link.getSku());
        assertEquals("adidas Performance Core 18 Kapuzenpullover Herren NEU", link.getName());
        assertEquals("24.95", link.getPrice().toString());
        assertEquals("adidas Performance", link.getBrand());
        assertEquals("outfitter-shop", link.getSeller());
        assertEquals("Kein Versand nach Türkei", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("292503186932", link.getSku());
        assertEquals("Einhell GE-HH 18 Li T Kit Akku-Heckenschere 3.0 Ah Power-X-Change Strauchschere", link.getName());
        assertEquals("99.95", link.getPrice().toString());
        assertEquals("Einhell", link.getBrand());
        assertEquals("elektro-himmel", link.getSeller());
        assertEquals("Kein Versand nach Türkei", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
