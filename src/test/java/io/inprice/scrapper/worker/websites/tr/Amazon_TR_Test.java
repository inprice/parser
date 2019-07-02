package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Amazon_TR_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "tr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07JBZSZN2", link.getSku());
        assertEquals("Alba Star RETRO Olta Makinesi 10Kg Test Edilmiş Drag Gücü, 5+1 Bilyalı, Paslanmaz Gövde ve Rulmanlar, 390Gr ağırlık Olta Makinesi 50", link.getName());
        assertEquals("130.00", link.getPrice().toString());
        assertEquals("Alba Star", link.getBrand());
        assertEquals("Deniz Dükkanı", link.getSeller());
        assertEquals("+ Kargo BEDAVA", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07B598ZHX", link.getSku());
        assertEquals("Oral-B Diş Fırçası Yedek Başlığı Cross Action, 4 adet", link.getName());
        assertEquals("54.90", link.getPrice().toString());
        assertEquals("Oral B", link.getBrand());
        assertEquals("E-COSMO", link.getSeller());
        assertEquals("Kargo BEDAVA Ayrıntılar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07GF31D58", link.getSku());
        assertEquals("Paşabahçe Castle Şarap Seti, 5 Parça", link.getName());
        assertEquals("55.90", link.getPrice().toString());
        assertEquals("Paşabahçe", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Kargo BEDAVA Ayrıntılar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B0774YXJNC", link.getSku());
        assertEquals("Huawei AM08 Little Swan Kablosuz Bluetooth Hoparlör, Beyaz", link.getName());
        assertEquals("128.90", link.getPrice().toString());
        assertEquals("Huawei", link.getBrand());
        assertEquals("Hocotech", link.getSeller());
        assertEquals("+ Kargo BEDAVA", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
