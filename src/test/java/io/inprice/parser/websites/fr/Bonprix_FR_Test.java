package io.inprice.parser.websites.fr;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_FR_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.parser.websites.xx.Bonprix(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("94137181_28538595", competitor.getSku());
        assertEquals("Drap de hammam Flamant Rose", competitor.getName());
        assertEquals("10.99", competitor.getPrice().toString());
        assertEquals("bpc living", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("97930495_25736711", competitor.getSku());
        assertEquals("Soutien-gorge minimiseur", competitor.getName());
        assertEquals("12.99", competitor.getPrice().toString());
        assertEquals("bpc selection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("95290081_27241607", competitor.getSku());
        assertEquals("Slippers de Lico", competitor.getName());
        assertEquals("29.99", competitor.getPrice().toString());
        assertEquals("Lico", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("96923395_24435348", competitor.getSku());
        assertEquals("Couvre-lit Samira", competitor.getName());
        assertEquals("14.99", competitor.getPrice().toString());
        assertEquals("bpc living", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € par commande Plus d’informations >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
