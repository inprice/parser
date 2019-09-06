package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HepsiBurada_TR_Test {

    private final String SITE_NAME = "hepsiburada";
    private final String COUNTRY_CODE = "tr";

    private final HepsiBurada site = new HepsiBurada(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("TELREVASEG0001", link.getSku());
        assertEquals("Everest Vr-0021 Glasses Vr Box Sanal Gerçeklik Gözlüğü", link.getName());
        assertEquals("101.63", link.getPrice().toString());
        assertEquals("Everest", link.getBrand());
        assertEquals("Hepsiburada", link.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("HBV000003SX33", link.getSku());
        assertEquals("Artdeco Akrilik Boya 140 Ml Beyaz Y-070Y-3619", link.getName());
        assertEquals("5.56", link.getPrice().toString());
        assertEquals("Artdeco", link.getBrand());
        assertEquals("Hepsiburada", link.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("HBV00000JCM2V", link.getSku());
        assertEquals("Hepsiburada Home Modern Style 35 x 35 cm Cam Duvar Saati GLA001", link.getName());
        assertEquals("38.81", link.getPrice().toString());
        assertEquals("Hepsiburada Home", link.getBrand());
        assertEquals("Hepsiburada", link.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("HBV00000F70TC", link.getSku());
        assertEquals("Hepsiburada Home Kombo Sürgülü Duş Seti", link.getName());
        assertEquals("34.90", link.getPrice().toString());
        assertEquals("Hepsiburada Home", link.getBrand());
        assertEquals("Hepsiburada", link.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
