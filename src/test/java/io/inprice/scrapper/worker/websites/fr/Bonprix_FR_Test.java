package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class Bonprix_FR_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("94137181_28538595", link.getSku());
        assertEquals("Drap de hammam Flamant Rose", link.getName());
        assertEquals("10.99", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("97930495_25736711", link.getSku());
        assertEquals("Soutien-gorge minimiseur", link.getName());
        assertEquals("12.99", link.getPrice().toString());
        assertEquals("bpc selection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("95290081_27241607", link.getSku());
        assertEquals("Slippers de Lico", link.getName());
        assertEquals("29.99", link.getPrice().toString());
        assertEquals("Lico", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("96923395_24435348", link.getSku());
        assertEquals("Couvre-lit Samira", link.getName());
        assertEquals("14.99", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
