package io.inprice.parser.websites.es;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Gigas101_ES_Test {

    private final String SITE_NAME = "101gigas";
    private final String COUNTRY_CODE = "es";

    private final Gigas101 site = new Gigas101();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("9893851", link.getSku());
        assertEquals("MANDO INALÁMBRICO DUALSHOCK 4 SONY CUH-ZCT2E WAVE BLUE - VALIDO PARA PS4 - CONECTOR AURICULAR - ALTAVOZ INTEGRADO - PANEL TÁCTIL", link.getName());
        assertEquals("51.80", link.getPrice().toString());
        assertEquals("SONY", link.getBrand());
        assertEquals("101Gigas", link.getSeller());
        assertEquals("Envío: 6,95 € +info", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("ppPOS8058", link.getSku());
        assertEquals("approx Impresora Tiquets appPOS8058 Dual Usb/Corte", link.getName());
        assertEquals("124.80", link.getPrice().toString());
        assertEquals("APPROX", link.getBrand());
        assertEquals("101Gigas", link.getSeller());
        assertEquals("Envío: 6,95 € +info", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("WDS100T3XHC", link.getSku());
        assertEquals("Western Digital WD Black SN750 NVMe SSD WDS100T3XHC - Unidad en estado sólido - 1 TB - interno - M.2 2280 - PCI Expr", link.getName());
        assertEquals("300.80", link.getPrice().toString());
        assertEquals("WESTERN DIGITAL", link.getBrand());
        assertEquals("101Gigas", link.getSeller());
        assertEquals("Envío: 6,95 € +info", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("90YV0AQ1-M0NA00", link.getSku());
        assertEquals("Asus Radeon RX 580 Dual OC 8GB GDDR5", link.getName());
        assertEquals("203.80", link.getPrice().toString());
        assertEquals("ASUS", link.getBrand());
        assertEquals("101Gigas", link.getSeller());
        assertEquals("Envío: 6,95 € +info", link.getShipment());
        assertNull(link.getSpecList());
    }

}