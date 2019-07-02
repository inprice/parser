package io.inprice.scrapper.worker.websites.es;

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
public class MediaMarkt_ES_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.MediaMarkt(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.OUT_OF_STOCK, link.getStatus());
        assertEquals("1299751", link.getSku());
        assertEquals("Memoria USB 64 GB - Sandisk 139789 Ultra Flair, USB 3.0, Velocidad hasta 150mb/sg", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("SANDISK", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("más envío 1,99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1272050", link.getSku());
        assertEquals("Disco duro de 2TB - WD Elements, 2.5 pulgadas", link.getName());
        assertEquals("68.00", link.getPrice().toString());
        assertEquals("WESTERN DIGITAL", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("más envío 1,99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1275902", link.getSku());
        assertEquals("Cafetera - De Longhi EC7.1 Potencia 800W, Sistema Cappuccino, Tapón de seguridad", link.getName());
        assertEquals("64.90", link.getPrice().toString());
        assertEquals("DE LONGHI", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("más envío 2,99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.OUT_OF_STOCK, link.getStatus());
        assertEquals("1405935", link.getSku());
        assertEquals("PS4 Marvel S Spider-Man", link.getName());
        assertEquals("24.90", link.getPrice().toString());
        assertEquals("SONY COMPUTER ENT. S.A. (SOFT)", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("más envío 1,99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
