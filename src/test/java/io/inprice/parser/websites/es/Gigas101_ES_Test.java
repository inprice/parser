package io.inprice.parser.websites.es;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Gigas101_ES_Test {

    private final String SITE_NAME = "101gigas";
    private final String COUNTRY_CODE = "es";

    private final Gigas101 site = new Gigas101(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9893851", competitor.getSku());
        assertEquals("MANDO INALÁMBRICO DUALSHOCK 4 SONY CUH-ZCT2E WAVE BLUE - VALIDO PARA PS4 - CONECTOR AURICULAR - ALTAVOZ INTEGRADO - PANEL TÁCTIL", competitor.getName());
        assertEquals("51.80", competitor.getPrice().toString());
        assertEquals("SONY", competitor.getBrand());
        assertEquals("101Gigas", competitor.getSeller());
        assertEquals("Envío: 6,95 € +info", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("ppPOS8058", competitor.getSku());
        assertEquals("approx Impresora Tiquets appPOS8058 Dual Usb/Corte", competitor.getName());
        assertEquals("124.80", competitor.getPrice().toString());
        assertEquals("APPROX", competitor.getBrand());
        assertEquals("101Gigas", competitor.getSeller());
        assertEquals("Envío: 6,95 € +info", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("WDS100T3XHC", competitor.getSku());
        assertEquals("Western Digital WD Black SN750 NVMe SSD WDS100T3XHC - Unidad en estado sólido - 1 TB - interno - M.2 2280 - PCI Expr", competitor.getName());
        assertEquals("300.80", competitor.getPrice().toString());
        assertEquals("WESTERN DIGITAL", competitor.getBrand());
        assertEquals("101Gigas", competitor.getSeller());
        assertEquals("Envío: 6,95 € +info", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("90YV0AQ1-M0NA00", competitor.getSku());
        assertEquals("Asus Radeon RX 580 Dual OC 8GB GDDR5", competitor.getName());
        assertEquals("203.80", competitor.getPrice().toString());
        assertEquals("ASUS", competitor.getBrand());
        assertEquals("101Gigas", competitor.getSeller());
        assertEquals("Envío: 6,95 € +info", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

}
