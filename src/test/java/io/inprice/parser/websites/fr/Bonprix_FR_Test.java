package io.inprice.parser.websites.fr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_FR_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.parser.websites.xx.Bonprix();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("96923395_24435348", link.getSku());
        assertEquals("Couvre-lit Samira", link.getName());
        assertEquals("14.99", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}