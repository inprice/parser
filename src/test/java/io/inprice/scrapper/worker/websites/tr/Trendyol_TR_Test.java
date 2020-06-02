package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Trendyol_TR_Test {

    private final String SITE_NAME = "trendyol";
    private final String COUNTRY_CODE = "tr";

    private final Trendyol site = new Trendyol(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1263059", competitor.getSku());
        assertEquals("Hacim Sağlayan Siyah Maskara - Colossal Go Extreme Volum Express Mascara 30114319", competitor.getName());
        assertEquals("29.95", competitor.getPrice().toString());
        assertEquals("Maybelline", competitor.getBrand());
        assertEquals("L'Oreal Türkiye", competitor.getSeller());
        assertEquals("L'Oreal Türkiye tarafından gönderilecektir.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3390771", competitor.getSku());
        assertEquals("Unisex Sırt Çantası - Y Nk Academy Team - Ba5773-010 BA5773-010", competitor.getName());
        assertEquals("118.00", competitor.getPrice().toString());
        assertEquals("Nike", competitor.getBrand());
        assertEquals("Dream Sport", competitor.getSeller());
        assertEquals("Kargo Bedava", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6733226", competitor.getSku());
        assertEquals("Erkek Lacivert Kemer L6149AZ.19SM.NV42", competitor.getName());
        assertEquals("25.99", competitor.getPrice().toString());
        assertEquals("Defacto", competitor.getBrand());
        assertEquals("DeFacto", competitor.getSeller());
        assertEquals("DeFacto tarafından gönderilecektir.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3058612", competitor.getSku());
        assertEquals("Altus AL 6100 L A+++ 1000 Devir 6 kg Çamaşır Makinesi AL6100L", competitor.getName());
        assertEquals("1199.00", competitor.getPrice().toString());
        assertEquals("Altus", competitor.getBrand());
        assertEquals("Beyaz Live", competitor.getSeller());
        assertEquals("Kargo Bedava", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
