package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Laredoute_FR_Test {

    private final String SITE_NAME = "laredoute";
    private final String COUNTRY_CODE = "fr";

    private final Laredoute site = new Laredoute(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("500787340", competitor.getSku());
        assertEquals("Sommier de relaxation électrique", competitor.getName());
        assertEquals("431.20", competitor.getPrice().toString());
        assertEquals("REVERIE", competitor.getBrand());
        assertEquals("La Redoute", competitor.getSeller());
        assertEquals("Livraison à domicile : 39,00 €", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("515599021", competitor.getSku());
        assertEquals("Baskets N-5923", competitor.getName());
        assertEquals("59.94", competitor.getPrice().toString());
        assertEquals("adidas Originals", competitor.getBrand());
        assertEquals("La Redoute", competitor.getSeller());
        assertEquals("Livraison gratuite en point Relais Colis ® dès 29€*", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("527882400", competitor.getSku());
        assertEquals("Lot de 20 serviettes papier champignon", competitor.getName());
        assertEquals("2.40", competitor.getPrice().toString());
        assertEquals("DOUCEUR D&#39;INTÉRIEUR", competitor.getBrand());
        assertEquals("1001 Kdo", competitor.getSeller());
        assertEquals("Livrable en point Relais Colis ® : 3,90 €", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("500779771", competitor.getSku());
        assertEquals("Fauteuil de jardin, Joalie", competitor.getName());
        assertEquals("104.38", competitor.getPrice().toString());
        assertEquals("LA REDOUTE INTERIEURS", competitor.getBrand());
        assertEquals("La Redoute", competitor.getSeller());
        assertEquals("Livraison à domicile : 19,00 €", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

}
