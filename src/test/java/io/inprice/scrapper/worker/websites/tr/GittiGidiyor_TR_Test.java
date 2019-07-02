package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.nl.Wehkamp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class GittiGidiyor_TR_Test {

    private final String SITE_NAME = "gittigidiyor";
    private final String COUNTRY_CODE = "tr";

    private final GittiGidiyor site = new GittiGidiyor(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("445833337", link.getSku());
        assertEquals("Huawei Mate 20 Lite 64 GB Duos Cep Telefonu", link.getName());
        assertEquals("2029.00", link.getPrice().toString());
        assertEquals("Huawei", link.getBrand());
        assertEquals("ceppaket", link.getSeller());
        assertEquals("Alıcı Öder - 2-3 gün içinde kargolanır Tüm Türkiye: 9,00 TL", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("450770874", link.getSku());
        assertEquals("GÜNEŞ ENERJİLİ SOLAR DIŞ MEKAN AYDINLATMASI DEKORATİF 30 LED 5 METRE RENKLİ ANİMASYONLU", link.getName());
        assertEquals("99.99", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("cocukbebekevofis", link.getSeller());
        assertEquals("Ücretsiz - Aynı Gün Kargolanır", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("426398848", link.getSku());
        assertEquals("TÜP ŞEKLİNDE,50 adet, MANTAR TIPALI CAM ŞİŞE, ÜCRETSİZ KARGO", link.getName());
        assertEquals("71.99", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("birkanz", link.getSeller());
        assertEquals("Ücretsiz - Aynı Gün Kargolanır", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("409029831", link.getSku());
        assertEquals("Aden X67 Fly More Combo 2K (3 Bataryalı Set)", link.getName());
        assertEquals("1249.00", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("hubsan", link.getSeller());
        assertEquals("Ücretsiz - Aynı Gün Kargolanır", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
