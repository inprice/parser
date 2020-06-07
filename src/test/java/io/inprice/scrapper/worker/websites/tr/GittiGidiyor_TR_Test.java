package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GittiGidiyor_TR_Test {

    private final String SITE_NAME = "gittigidiyor";
    private final String COUNTRY_CODE = "tr";

    private final GittiGidiyor site = new GittiGidiyor(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("445833337", competitor.getSku());
        assertEquals("Huawei Mate 20 Lite 64 GB Duos Cep Telefonu", competitor.getName());
        assertEquals("2029.00", competitor.getPrice().toString());
        assertEquals("Huawei", competitor.getBrand());
        assertEquals("ceppaket", competitor.getSeller());
        assertEquals("Alıcı Öder - 2-3 gün içinde kargolanır Tüm Türkiye: 9,00 TL", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("450770874", competitor.getSku());
        assertEquals("GÜNEŞ ENERJİLİ SOLAR DIŞ MEKAN AYDINLATMASI DEKORATİF 30 LED 5 METRE RENKLİ ANİMASYONLU", competitor.getName());
        assertEquals("99.99", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("cocukbebekevofis", competitor.getSeller());
        assertEquals("Ücretsiz - Aynı Gün Kargolanır", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("426398848", competitor.getSku());
        assertEquals("TÜP ŞEKLİNDE,50 adet, MANTAR TIPALI CAM ŞİŞE, ÜCRETSİZ KARGO", competitor.getName());
        assertEquals("71.99", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("birkanz", competitor.getSeller());
        assertEquals("Ücretsiz - Aynı Gün Kargolanır", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("409029831", competitor.getSku());
        assertEquals("Aden X67 Fly More Combo 2K (3 Bataryalı Set)", competitor.getName());
        assertEquals("1249.00", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("hubsan", competitor.getSeller());
        assertEquals("Ücretsiz - Aynı Gün Kargolanır", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_5() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("453332669", competitor.getSku());
        assertEquals("Bosch TDA3024010 Ütü", competitor.getName());
        assertEquals("258.99", competitor.getPrice().toString());
        assertEquals("Bosch", competitor.getBrand());
        assertEquals("sepet-indirimi", competitor.getSeller());
        assertEquals("Ücretsiz", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
