package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class Teknosa_TR_Test {

    private final String SITE_NAME = "teknosa";
    private final String COUNTRY_CODE = "tr";

    private final Teknosa site = new Teknosa(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("135177598", link.getSku());
        assertEquals("Gopro Hero7 White   5GPR CHDHB 601 Aksiyon Kamera", link.getName());
        assertEquals("1599.00", link.getPrice().toString());
        assertEquals("GOPRO", link.getBrand());
        assertEquals("Teknosa", link.getSeller());
        assertEquals("Mağazada teslim", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("125076139", link.getSku());
        assertEquals("Apple iPhone 7 32 GB Siyah Akıllı Telefon", link.getName());
        assertEquals("3699.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Teknosa", link.getSeller());
        assertEquals("Mağazada teslim", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("125430443", link.getSku());
        assertEquals("Tp Link Tl Wa855Re 300Mbps N Kablosuz 2 Harici Antenli Kompakt Access Point Ve Menzil Genişletici", link.getName());
        assertEquals("159.00", link.getPrice().toString());
        assertEquals("TP-LINK", link.getBrand());
        assertEquals("Teknosa", link.getSeller());
        assertEquals("Mağazada teslim", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("125089771", link.getSku());
        assertEquals("Inca Lapetos Ikg 447 Silent Software Gaming KeyboardSessiz Tuş Makrolu", link.getName());
        assertEquals("159.00", link.getPrice().toString());
        assertEquals("INCA", link.getBrand());
        assertEquals("Teknosa", link.getSeller());
        assertEquals("Mağazada teslim", link.getShipment());
        assertNull(link.getSpecList());
    }

}
