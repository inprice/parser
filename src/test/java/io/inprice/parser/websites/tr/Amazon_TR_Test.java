package io.inprice.parser.websites.tr;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_TR_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "tr";

    private final Website site = new io.inprice.parser.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07JBZSZN2", competitor.getSku());
        assertEquals("Alba Star RETRO Olta Makinesi 10Kg Test Edilmiş Drag Gücü, 5+1 Bilyalı, Paslanmaz Gövde ve Rulmanlar, 390Gr ağırlık Olta Makinesi 50", competitor.getName());
        assertEquals("130.00", competitor.getPrice().toString());
        assertEquals("Alba Star", competitor.getBrand());
        assertEquals("Deniz Dükkanı", competitor.getSeller());
        assertEquals("+ Kargo BEDAVA", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07B598ZHX", competitor.getSku());
        assertEquals("Oral-B Diş Fırçası Yedek Başlığı Cross Action, 4 adet", competitor.getName());
        assertEquals("54.90", competitor.getPrice().toString());
        assertEquals("Oral B", competitor.getBrand());
        assertEquals("E-COSMO", competitor.getSeller());
        assertEquals("Kargo BEDAVA Ayrıntılar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07GF31D58", competitor.getSku());
        assertEquals("Paşabahçe Castle Şarap Seti, 5 Parça", competitor.getName());
        assertEquals("55.90", competitor.getPrice().toString());
        assertEquals("Paşabahçe", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Kargo BEDAVA Ayrıntılar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B0774YXJNC", competitor.getSku());
        assertEquals("Huawei AM08 Little Swan Kablosuz Bluetooth Hoparlör, Beyaz", competitor.getName());
        assertEquals("128.90", competitor.getPrice().toString());
        assertEquals("Huawei", competitor.getBrand());
        assertEquals("Hocotech", competitor.getSeller());
        assertEquals("+ Kargo BEDAVA", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
