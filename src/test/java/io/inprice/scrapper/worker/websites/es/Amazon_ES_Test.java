package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_ES_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07FYZ9WF9", competitor.getSku());
        assertEquals("Xiaomi Mi Band 3 Pulsera de Actividad Inteligente Rastreador Deportes Deportiva con Pulsómetro Monitor de Ritmo Cardíaco 0,78 Pulgadas OLED Pantalla Táctil Pronóstico del Tiempo Negro", competitor.getName());
        assertEquals("39.99", competitor.getPrice().toString());
        assertEquals("Xiaomi", competitor.getBrand());
        assertEquals("Edwaybuy Europe", competitor.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00ZFOB4BK", competitor.getSku());
        assertEquals("Dodow - Metrónomo luminoso para ayudarte a dormir más rápidamente - Blanco", competitor.getName());
        assertEquals("48.44", competitor.getPrice().toString());
        assertEquals("Dodow", competitor.getBrand());
        assertEquals("shoppystore", competitor.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07HB1RYG7", competitor.getSku());
        assertEquals("Travis Touch - Traductor Inteligente de Bolsillo a 105 Idiomas Con Pantalla Táctil, 4G LTE, Hotspot y Carga Inalámbrica", competitor.getName());
        assertEquals("199.00", competitor.getPrice().toString());
        assertEquals("Travis", competitor.getBrand());
        assertEquals("Travis The Translator", competitor.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B01L9KX5RY", competitor.getSku());
        assertEquals("Apple iPhone 6 Gris Espacial 16GB Smartphone Libre (Reacondicionado)", competitor.getName());
        assertEquals("153.90", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("NEXT MOBILES", competitor.getSeller());
        assertEquals("Envío GRATIS. Ver detalles", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
