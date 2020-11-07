package io.inprice.parser.websites.es;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_ES_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.parser.websites.xx.Ebay();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("264218368315", link.getSku());
        assertEquals("ORDENADOR PORTATIL LENOVO INTEL 4GB 500GB WIFI WINDOWS 10 + OFFICE +ANTIVIRUS", link.getName());
        assertEquals("205.95", link.getPrice().toString());
        assertEquals("Lenovo", link.getBrand());
        assertEquals("boxterass", link.getSeller());
        assertEquals("No se realizan envíos a Turquía", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("202113774000", link.getSku());
        assertEquals("Taladro Atornillador a Bateria 18V Litio sin Cable con Maletin", link.getName());
        assertEquals("34.99", link.getPrice().toString());
        assertEquals("T-LoVendo", link.getBrand());
        assertEquals("t-lovendo_com", link.getSeller());
        assertEquals("No se puede enviar a Turquía", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("202346134548", link.getSku());
        assertEquals("Silla Gaming Oficina Racing Sillon gamer Despacho Profesional Videojuegos PC nue", link.getName());
        assertEquals("89.99", link.getPrice().toString());
        assertEquals("T-LoVendo", link.getBrand());
        assertEquals("t-lovendo_com", link.getSeller());
        assertEquals("No se puede enviar a Turquía", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("323291864624", link.getSku());
        assertEquals("iRobot Roomba 696 robot aspirador sin bolsa", link.getName());
        assertEquals("229.99", link.getPrice().toString());
        assertEquals("iRobot", link.getBrand());
        assertEquals("ofertas3b", link.getSeller());
        assertEquals("No se puede enviar a Turquía", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}