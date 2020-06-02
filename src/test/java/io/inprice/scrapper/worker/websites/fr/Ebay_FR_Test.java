package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_FR_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("312590438787", competitor.getSku());
        assertEquals("2/4/8/16/32/64/128/200 Go SanDisk Ultra Extreme Pro C10 Micro SD SDHC/SDXC Card", competitor.getName());
        assertEquals("2.89", competitor.getPrice().toString());
        assertEquals("Sandisk", competitor.getBrand());
        assertEquals("nsw-act", competitor.getSeller());
        assertEquals("Il se peut que la livraison ne soit pas offerte vers Turquie", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("201932684504", competitor.getSku());
        assertEquals("4 TAPPI COPRIMOZZO PER MERCEDES BENZ CERCHI IN LEGA 75MM CLASSE A B C E CLA CLK", competitor.getName());
        assertEquals("15.37", competitor.getPrice().toString());
        assertEquals("COMPATIBILE PER MERCEDES", competitor.getBrand());
        assertEquals("lombardoshop", competitor.getSeller());
        assertEquals("Livraison vers Turquie. Consultez la description de l'objet ou contactez le vendeur pour en savoir plus sur les options de livraison. | Détails", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("163323425100", competitor.getSku());
        assertEquals("PHILIPS Multicuiseur Viva Collection HD3158/77 16 programmes Livre recettes 980W", competitor.getName());
        assertEquals("63.90", competitor.getPrice().toString());
        assertEquals("Philips", competitor.getBrand());
        assertEquals("boutique-philips", competitor.getSeller());
        assertEquals("Il se peut que la livraison ne soit pas offerte vers Turquie", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("303181010478", competitor.getSku());
        assertEquals("14 - DOUDOU PELUCHE OURS MARRON BRUN HARRY ENESCO NOEUD VICHY NEUF", competitor.getName());
        assertEquals("20.00", competitor.getPrice().toString());
        assertEquals("Kimbaloo", competitor.getBrand());
        assertEquals("petitbiscuit12345", competitor.getSeller());
        assertEquals("7,00 EUR La Poste - Lettre Suivie Internationale", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
