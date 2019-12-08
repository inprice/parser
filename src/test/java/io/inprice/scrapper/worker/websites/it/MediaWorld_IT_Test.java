package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaWorld_IT_Test {

    private final String SITE_NAME = "mediaworld";
    private final String COUNTRY_CODE = "it";

    private final MediaWorld site = new MediaWorld(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("p-100094", link.getSku());
        assertEquals("NIKON D3500+AF-P 18/55VR+ZAINO BLACK (Fotocamera Reflex con Obiettivo)", link.getName());
        assertEquals("399.00", link.getPrice().toString());
        assertEquals("NIKON", link.getBrand());
        assertEquals("Media World", link.getSeller());
        assertEquals("A casa tua entro mercoledì 19 giugno Consegna Standard € 7.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("p-718631", link.getSku());
        assertEquals("AVENT SCD501/00 (Baby Monitor DECT Philips AVENT)", link.getName());
        assertEquals("52.99", link.getPrice().toString());
        assertEquals("AVENT", link.getBrand());
        assertEquals("Media World", link.getSeller());
        assertEquals("A casa tua entro mercoledì 19 giugno Consegna Standard € 4.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("p-736235", link.getSku());
        assertEquals("BOSCH SMZ5300 (Accessorio speciale per lavastoviglie)", link.getName());
        assertEquals("29.99", link.getPrice().toString());
        assertEquals("BOSCH", link.getBrand());
        assertEquals("Media World", link.getSeller());
        assertEquals("A casa tua entro giovedì 04 luglio Consegna Standard € 4.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("p-641394", link.getSku());
        assertEquals("GIGASET DA710 Black (Telefoni con Filo)", link.getName());
        assertEquals("42.99", link.getPrice().toString());
        assertEquals("GIGASET", link.getBrand());
        assertEquals("Media World", link.getSeller());
        assertEquals("A casa tua entro mercoledì 19 giugno Consegna Standard € 4.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
