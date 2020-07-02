package io.inprice.parser.websites.es;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Electroking_ES_Test {

    private final String SITE_NAME = "electroking";
    private final String COUNTRY_CODE = "es";

    private final Electroking site = new Electroking(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1860221", competitor.getSku());
        assertEquals("Protector pantalla silver ht 1209", competitor.getName());
        assertEquals("2.18", competitor.getPrice().toString());
        assertEquals("SILVER SANZ", competitor.getBrand());
        assertEquals("Electroking", competitor.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1880281", competitor.getSku());
        assertEquals("Carro compra ROLSER termo mf 2+2 n/r", competitor.getName());
        assertEquals("52.03", competitor.getPrice().toString());
        assertEquals("ROLSER", competitor.getBrand());
        assertEquals("Electroking", competitor.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1861570", competitor.getSku());
        assertEquals("Vinoteca ORBEGOZO VT3010", competitor.getName());
        assertEquals("233.29", competitor.getPrice().toString());
        assertEquals("ORBEGOZO", competitor.getBrand());
        assertEquals("Electroking", competitor.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1788709", competitor.getSku());
        assertEquals("Puntero LOGITECH R400 wireless", competitor.getName());
        assertEquals("34.85", competitor.getPrice().toString());
        assertEquals("LOGITECH", competitor.getBrand());
        assertEquals("Electroking", competitor.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

}
