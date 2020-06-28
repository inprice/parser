package io.inprice.scrapper.parser.websites.tr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class N11_TR_Test {

    private final String SITE_NAME = "n11";
    private final String COUNTRY_CODE = "tr";

    private final N11 site = new N11(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("249823652", competitor.getSku());
        assertEquals("Seaflo Hidrofor Pompası 4.3 lt. / dak 12 V 35 psi", competitor.getName());
        assertEquals("123.55", competitor.getPrice().toString());
        assertEquals("Diğer", competitor.getBrand());
        assertEquals("eroldenizcilik", competitor.getSeller());
        assertEquals("Mağazaya özel 150 TL ve üzeri Ücretsiz Kargo", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("241174632", competitor.getSku());
        assertEquals("Bel İnceltici Korse Vücut Şekillendirici Doğum Sonrası Toparlayıc", competitor.getName());
        assertEquals("39.90", competitor.getPrice().toString());
        assertEquals("Giaxa", competitor.getBrand());
        assertEquals("ecogroup", competitor.getSeller());
        assertEquals("Ücretsiz Kargo", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("31175655", competitor.getSku());
        assertEquals("Gemici Çelik Erkek Zincir Kolye 7 mm 56 cm h081", competitor.getName());
        assertEquals("59.90", competitor.getPrice().toString());
        assertEquals("Takı Dükkanı", competitor.getBrand());
        assertEquals("takidukkani", competitor.getSeller());
        assertEquals("Ücretsiz Kargo", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("223682414", competitor.getSku());
        assertEquals("Genç Elitler Serisi Kutulu Özel Set (Ciltli 3 Kitap)", competitor.getName());
        assertEquals("65.84", competitor.getPrice().toString());
        assertEquals("Marie Lu", competitor.getBrand());
        assertEquals("pegasusyayinlari", competitor.getSeller());
        assertEquals("Mağazaya özel 100 TL ve üzeri Ücretsiz Kargo", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_5() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("428137685", competitor.getSku());
        assertEquals("Oppo Realme X2 Pro Duos 128 GB (İthalatçı Garantili)", competitor.getName());
        assertEquals("4899.00", competitor.getPrice().toString());
        assertEquals("Oppo", competitor.getBrand());
        assertEquals("Anatolium", competitor.getSeller());
        assertEquals("Ücretsiz Kargo", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
