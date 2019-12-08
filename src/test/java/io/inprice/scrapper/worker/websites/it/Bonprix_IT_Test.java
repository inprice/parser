package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_IT_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
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

        assertEquals(Status.AVAILABLE, link.getStatus());
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

        assertEquals(Status.AVAILABLE, link.getStatus());
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

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("95218581_27665897", link.getSku());
        assertEquals("Mocassino Kappa", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("Kappa", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
