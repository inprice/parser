package io.inprice.parser.websites.fr;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Rakuten_FR_Test {

    private final String SITE_NAME = "rakuten";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.parser.websites.xx.Rakuten(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("7311271629221", competitor.getSku());
        assertEquals("Sony Xperia 1 Dual SIM 128 Go Blanc", competitor.getName());
        assertEquals("809.00", competitor.getPrice().toString());
        assertEquals("Sony", competitor.getBrand());
        assertEquals("Importshop", competitor.getSeller());
        assertEquals("Livraison gratuite", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("8436571382764", competitor.getSku());
        assertEquals("Vélo Electrique Pliable Mr Urban Ebike 20' Black", competitor.getName());
        assertEquals("419.95", competitor.getPrice().toString());
        assertEquals("Moverace", competitor.getBrand());
        assertEquals("FLOATUP", competitor.getSeller());
        assertEquals("Livraison gratuite", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("5099747470929", competitor.getSku());
        assertEquals("HIStory-Past, Present And Future Book I", competitor.getName());
        assertEquals("3.22", competitor.getPrice().toString());
        assertEquals("Janet Jackson", competitor.getBrand());
        assertEquals("momox", competitor.getSeller());
        assertEquals("Livraison gratuite", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("0842776106179", competitor.getSku());
        assertEquals("Google Chromecast 3 - Récepteur multimédia numérique", competitor.getName());
        assertEquals("39.00", competitor.getPrice().toString());
        assertEquals("Google", competitor.getBrand());
        assertEquals("Boulanger", competitor.getSeller());
        assertEquals("Livraison gratuite", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
