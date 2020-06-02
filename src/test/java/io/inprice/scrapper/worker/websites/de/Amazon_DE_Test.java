package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_DE_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07HDSP11B", competitor.getSku());
        assertEquals("Vogelgaleria Sepiaschalen in verschiedenen Größen", competitor.getName());
        assertEquals("10.99", competitor.getPrice().toString());
        assertEquals("Vogelgaleria", competitor.getBrand());
        assertEquals("Vogelgaleria", competitor.getSeller());
        assertEquals("GRATIS-Versand für Bestellungen ab EUR 29 und Versand durch Amazon. Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B06ZXQV6P8", competitor.getSku());
        assertEquals("Amazon Echo (2. Gen.), Intelligenter Lautsprecher mit Alexa, Anthrazit Stoff", competitor.getName());
        assertEquals("79.99", competitor.getPrice().toString());
        assertEquals("Amazon", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Kostenlose Lieferung. Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07JFVSJNW", competitor.getSku());
        assertEquals("Victrola Pro Automatischer Plattenspieler USB Vinyl-zu-MP3-Aufnahme - Silber", competitor.getName());
        assertEquals("116.28", competitor.getPrice().toString());
        assertEquals("Victrola (DE)", competitor.getBrand());
        assertEquals("nrsolutions", competitor.getSeller());
        assertEquals("Kostenlose Lieferung. Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B0095FMJE6", competitor.getSku());
        assertEquals("Tassimo Jacobs Caffè Crema Classico XL, 5er Pack Kaffee T Discs (5 x 16 Getränke)", competitor.getName());
        assertEquals("21.95", competitor.getPrice().toString());
        assertEquals("Tassimo", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("See all offers", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
