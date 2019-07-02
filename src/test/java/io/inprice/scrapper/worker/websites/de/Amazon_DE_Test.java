package io.inprice.scrapper.worker.websites.de;

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
public class Amazon_DE_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07HDSP11B", link.getSku());
        assertEquals("Vogelgaleria Sepiaschalen in verschiedenen Größen", link.getName());
        assertEquals("10.99", link.getPrice().toString());
        assertEquals("Vogelgaleria", link.getBrand());
        assertEquals("Vogelgaleria", link.getSeller());
        assertEquals("GRATIS-Versand für Bestellungen ab EUR 29 und Versand durch Amazon. Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B06ZXQV6P8", link.getSku());
        assertEquals("Amazon Echo (2. Gen.), Intelligenter Lautsprecher mit Alexa, Anthrazit Stoff", link.getName());
        assertEquals("79.99", link.getPrice().toString());
        assertEquals("Amazon", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Kostenlose Lieferung. Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07JFVSJNW", link.getSku());
        assertEquals("Victrola Pro Automatischer Plattenspieler USB Vinyl-zu-MP3-Aufnahme - Silber", link.getName());
        assertEquals("116.28", link.getPrice().toString());
        assertEquals("Victrola (DE)", link.getBrand());
        assertEquals("nrsolutions", link.getSeller());
        assertEquals("Kostenlose Lieferung. Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B0095FMJE6", link.getSku());
        assertEquals("Tassimo Jacobs Caffè Crema Classico XL, 5er Pack Kaffee T Discs (5 x 16 Getränke)", link.getName());
        assertEquals("21.95", link.getPrice().toString());
        assertEquals("Tassimo", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("See all offers", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
