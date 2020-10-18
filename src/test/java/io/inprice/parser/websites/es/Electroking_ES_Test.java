package io.inprice.parser.websites.es;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Electroking_ES_Test {

    private final String SITE_NAME = "electroking";
    private final String COUNTRY_CODE = "es";

    private final Electroking site = new Electroking(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1860221", link.getSku());
        assertEquals("Protector pantalla silver ht 1209", link.getName());
        assertEquals("2.18", link.getPrice().toString());
        assertEquals("SILVER SANZ", link.getBrand());
        assertEquals("Electroking", link.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1880281", link.getSku());
        assertEquals("Carro compra ROLSER termo mf 2+2 n/r", link.getName());
        assertEquals("52.03", link.getPrice().toString());
        assertEquals("ROLSER", link.getBrand());
        assertEquals("Electroking", link.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1861570", link.getSku());
        assertEquals("Vinoteca ORBEGOZO VT3010", link.getName());
        assertEquals("233.29", link.getPrice().toString());
        assertEquals("ORBEGOZO", link.getBrand());
        assertEquals("Electroking", link.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1788709", link.getSku());
        assertEquals("Puntero LOGITECH R400 wireless", link.getName());
        assertEquals("34.85", link.getPrice().toString());
        assertEquals("LOGITECH", link.getBrand());
        assertEquals("Electroking", link.getSeller());
        assertEquals("Envío por Agencia de Transporte. Ver detalles", link.getShipment());
        assertNull(link.getSpecList());
    }

}
