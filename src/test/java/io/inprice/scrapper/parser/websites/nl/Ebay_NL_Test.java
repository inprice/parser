package io.inprice.scrapper.parser.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_NL_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("311777636467", competitor.getSku());
        assertEquals("Portable Travel 110lb / 50kg LCD Digital Hanging Luggage Scale Electronic Weight", competitor.getName());
        assertEquals("7.93", competitor.getPrice().toString());
        assertEquals("Fosmon", competitor.getBrand());
        assertEquals("sfplanet", competitor.getSeller());
        assertEquals("Gratis USPS First Class Mail International / First Class Package International Service", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("223526308937", competitor.getSku());
        assertEquals("Blade INDUCTRIX FPV Drone", competitor.getName());
        assertEquals("67.50", competitor.getPrice().toString());
        assertEquals("Blade", competitor.getBrand());
        assertEquals("onlycoolrc", competitor.getSeller());
        assertEquals("GBP 14,99 (ongeveer EUR 16,87) Voordelige verzendservice", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("332864473404", competitor.getSku());
        assertEquals("LAMPIONE STRADALE FARO FARETTO AD ENERGIA SOLARE A LED FOTOVOLTAICO LUCE LED", competitor.getName());
        assertEquals("22.99", competitor.getPrice().toString());
        assertEquals("Shopping in rete", competitor.getBrand());
        assertEquals("marlyn_shop", competitor.getSeller());
        assertEquals("EUR 29,90 Spedizione internazionale espressa", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("192924322253", competitor.getSku());
        assertEquals("Outil horloger ancien horloge XIX eme clock Morez Morbier Comtoise Watchmakers 5", competitor.getName());
        assertEquals("118.00", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("beffroi111", competitor.getSeller());
        assertEquals("EUR 18,00 La Poste - Lettre Prioritaire Internationale", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
