package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Rakuten_FR_Test {

    private final String SITE_NAME = "rakuten";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Rakuten(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("7311271629221", link.getSku());
        assertEquals("Sony Xperia 1 Dual SIM 128 Go Blanc", link.getName());
        assertEquals("809.00", link.getPrice().toString());
        assertEquals("Sony", link.getBrand());
        assertEquals("Importshop", link.getSeller());
        assertEquals("Livraison gratuite", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("8436571382764", link.getSku());
        assertEquals("Vélo Electrique Pliable Mr Urban Ebike 20' Black", link.getName());
        assertEquals("419.95", link.getPrice().toString());
        assertEquals("Moverace", link.getBrand());
        assertEquals("FLOATUP", link.getSeller());
        assertEquals("Livraison gratuite", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("5099747470929", link.getSku());
        assertEquals("HIStory-Past, Present And Future Book I", link.getName());
        assertEquals("3.22", link.getPrice().toString());
        assertEquals("Janet Jackson", link.getBrand());
        assertEquals("momox", link.getSeller());
        assertEquals("Livraison gratuite", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("0842776106179", link.getSku());
        assertEquals("Google Chromecast 3 - Récepteur multimédia numérique", link.getName());
        assertEquals("39.00", link.getPrice().toString());
        assertEquals("Google", link.getBrand());
        assertEquals("Boulanger", link.getSeller());
        assertEquals("Livraison gratuite", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
