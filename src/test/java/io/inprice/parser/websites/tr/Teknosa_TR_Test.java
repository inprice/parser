package io.inprice.parser.websites.tr;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Teknosa_TR_Test {

    private final String SITE_NAME = "teknosa";
    private final String COUNTRY_CODE = "tr";

    private final Teknosa site = new Teknosa(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("135177598", competitor.getSku());
        assertEquals("Gopro Hero7 White   5GPR CHDHB 601 Aksiyon Kamera", competitor.getName());
        assertEquals("1599.00", competitor.getPrice().toString());
        assertEquals("GOPRO", competitor.getBrand());
        assertEquals("Teknosa", competitor.getSeller());
        assertEquals("Mağazada teslim", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("125076139", competitor.getSku());
        assertEquals("Apple iPhone 7 32 GB Siyah Akıllı Telefon", competitor.getName());
        assertEquals("3699.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Teknosa", competitor.getSeller());
        assertEquals("Mağazada teslim", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("125430443", competitor.getSku());
        assertEquals("Tp Link Tl Wa855Re 300Mbps N Kablosuz 2 Harici Antenli Kompakt Access Point Ve Menzil Genişletici", competitor.getName());
        assertEquals("159.00", competitor.getPrice().toString());
        assertEquals("TP-LINK", competitor.getBrand());
        assertEquals("Teknosa", competitor.getSeller());
        assertEquals("Mağazada teslim", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("125089771", competitor.getSku());
        assertEquals("Inca Lapetos Ikg 447 Silent Software Gaming KeyboardSessiz Tuş Makrolu", competitor.getName());
        assertEquals("159.00", competitor.getPrice().toString());
        assertEquals("INCA", competitor.getBrand());
        assertEquals("Teknosa", competitor.getSeller());
        assertEquals("Mağazada teslim", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_5() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("125077074", competitor.getSku());
        assertEquals("Samsung Galaxy S10+ G975F 128GB Yeşil Akıllı Telefon", competitor.getName());
        assertEquals("6799.00", competitor.getPrice().toString());
        assertEquals("SAMSUNG", competitor.getBrand());
        assertEquals("Teknosa", competitor.getSeller());
        assertEquals("Mağazada teslim", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

}
