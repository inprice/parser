package io.inprice.parser.websites.it;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_IT_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.parser.websites.xx.Bonprix();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("90336995_24300002", link.getSku());
        assertEquals("Slip (pacco da 5)", link.getName());
        assertEquals("6.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("94636695_26215486", link.getSku());
        assertEquals("Tenda \"Tinta unita\" (pacco da 2)", link.getName());
        assertEquals("4.98", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("91545395_29806163", link.getSku());
        assertEquals("Lampade ad energia solare \"Flower\" (set 4 pezzi)", link.getName());
        assertEquals("29.99", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("95218581_27665897", link.getSku());
        assertEquals("Mocassino Kappa", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("Kappa", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
