package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HepsiBurada_TR_Test {

    private final String SITE_NAME = "hepsiburada";
    private final String COUNTRY_CODE = "tr";

    private final HepsiBurada site = new HepsiBurada(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("TELREVASEG0001", competitor.getSku());
        assertEquals("Everest Vr-0021 Glasses Vr Box Sanal Gerçeklik Gözlüğü", competitor.getName());
        assertEquals("101.63", competitor.getPrice().toString());
        assertEquals("Everest", competitor.getBrand());
        assertEquals("Hepsiburada", competitor.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("HBV000003SX33", competitor.getSku());
        assertEquals("Artdeco Akrilik Boya 140 Ml Beyaz Y-070Y-3619", competitor.getName());
        assertEquals("5.56", competitor.getPrice().toString());
        assertEquals("Artdeco", competitor.getBrand());
        assertEquals("Hepsiburada", competitor.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("HBV00000JCM2V", competitor.getSku());
        assertEquals("Hepsiburada Home Modern Style 35 x 35 cm Cam Duvar Saati GLA001", competitor.getName());
        assertEquals("38.81", competitor.getPrice().toString());
        assertEquals("Hepsiburada Home", competitor.getBrand());
        assertEquals("Hepsiburada", competitor.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("HBV00000F70TC", competitor.getSku());
        assertEquals("Hepsiburada Home Kombo Sürgülü Duş Seti", competitor.getName());
        assertEquals("34.90", competitor.getPrice().toString());
        assertEquals("Hepsiburada Home", competitor.getBrand());
        assertEquals("Hepsiburada", competitor.getSeller());
        assertEquals("50 TL ve üzeri alışverişlerde kargo bedava!", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
