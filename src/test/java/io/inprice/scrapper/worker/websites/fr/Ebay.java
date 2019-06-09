package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Ebay {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("312590438787", link.getSku());
        assertEquals("2/4/8/16/32/64/128/200 Go SanDisk Ultra Extreme Pro C10 Micro SD SDHC/SDXC Card", link.getName());
        assertEquals("2.89", link.getPrice().toString());
        assertEquals("Sandisk", link.getBrand());
        assertEquals("nsw-act", link.getSeller());
        assertEquals("Il se peut que la livraison ne soit pas offerte vers Turquie", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("201932684504", link.getSku());
        assertEquals("4 TAPPI COPRIMOZZO PER MERCEDES BENZ CERCHI IN LEGA 75MM CLASSE A B C E CLA CLK", link.getName());
        assertEquals("15.37", link.getPrice().toString());
        assertEquals("COMPATIBILE PER MERCEDES", link.getBrand());
        assertEquals("lombardoshop", link.getSeller());
        assertEquals("Livraison vers Turquie. Consultez la description de l'objet ou contactez le vendeur pour en savoir plus sur les options de livraison. | Détails", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("163323425100", link.getSku());
        assertEquals("PHILIPS Multicuiseur Viva Collection HD3158/77 16 programmes Livre recettes 980W", link.getName());
        assertEquals("63.90", link.getPrice().toString());
        assertEquals("Philips", link.getBrand());
        assertEquals("boutique-philips", link.getSeller());
        assertEquals("Il se peut que la livraison ne soit pas offerte vers Turquie", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("303181010478", link.getSku());
        assertEquals("14 - DOUDOU PELUCHE OURS MARRON BRUN HARRY ENESCO NOEUD VICHY NEUF", link.getName());
        assertEquals("20.00", link.getPrice().toString());
        assertEquals("Kimbaloo", link.getBrand());
        assertEquals("petitbiscuit12345", link.getSeller());
        assertEquals("7,00 EUR La Poste - Lettre Suivie Internationale", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
