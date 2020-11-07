package io.inprice.parser.websites.es;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_ES_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.parser.websites.xx.Amazon();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07FYZ9WF9", link.getSku());
        assertEquals("Xiaomi Mi Band 3 Pulsera de Actividad Inteligente Rastreador Deportes Deportiva con Pulsómetro Monitor de Ritmo Cardíaco 0,78 Pulgadas OLED Pantalla Táctil Pronóstico del Tiempo Negro", link.getName());
        assertEquals("39.99", link.getPrice().toString());
        assertEquals("Xiaomi", link.getBrand());
        assertEquals("Edwaybuy Europe", link.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B00ZFOB4BK", link.getSku());
        assertEquals("Dodow - Metrónomo luminoso para ayudarte a dormir más rápidamente - Blanco", link.getName());
        assertEquals("48.44", link.getPrice().toString());
        assertEquals("Dodow", link.getBrand());
        assertEquals("shoppystore", link.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07HB1RYG7", link.getSku());
        assertEquals("Travis Touch - Traductor Inteligente de Bolsillo a 105 Idiomas Con Pantalla Táctil, 4G LTE, Hotspot y Carga Inalámbrica", link.getName());
        assertEquals("199.00", link.getPrice().toString());
        assertEquals("Travis", link.getBrand());
        assertEquals("Travis The Translator", link.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B01L9KX5RY", link.getSku());
        assertEquals("Apple iPhone 6 Gris Espacial 16GB Smartphone Libre (Reacondicionado)", link.getName());
        assertEquals("153.90", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("NEXT MOBILES", link.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
