package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_IT_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("90336995_24300002", competitor.getSku());
        assertEquals("Slip (pacco da 5)", competitor.getName());
        assertEquals("6.99", competitor.getPrice().toString());
        assertEquals("bpc bonprix collection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("94636695_26215486", competitor.getSku());
        assertEquals("Tenda \"Tinta unita\" (pacco da 2)", competitor.getName());
        assertEquals("4.98", competitor.getPrice().toString());
        assertEquals("bpc living", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("91545395_29806163", competitor.getSku());
        assertEquals("Lampade ad energia solare \"Flower\" (set 4 pezzi)", competitor.getName());
        assertEquals("29.99", competitor.getPrice().toString());
        assertEquals("bpc living", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("95218581_27665897", competitor.getSku());
        assertEquals("Mocassino Kappa", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("Kappa", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 5,90 per ordine Informazioni >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
