package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Trendyol_TR_Test {

    private final String SITE_NAME = "trendyol";
    private final String COUNTRY_CODE = "tr";

    private final Trendyol site = new Trendyol(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1263059", link.getSku());
        assertEquals("Hacim Sağlayan Siyah Maskara - Colossal Go Extreme Volum Express Mascara 30114319", link.getName());
        assertEquals("29.95", link.getPrice().toString());
        assertEquals("Maybelline", link.getBrand());
        assertEquals("L'Oreal Türkiye", link.getSeller());
        assertEquals("L'Oreal Türkiye tarafından gönderilecektir.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("3390771", link.getSku());
        assertEquals("Unisex Sırt Çantası - Y Nk Academy Team - Ba5773-010 BA5773-010", link.getName());
        assertEquals("118.00", link.getPrice().toString());
        assertEquals("Nike", link.getBrand());
        assertEquals("Dream Sport", link.getSeller());
        assertEquals("Kargo Bedava", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("6733226", link.getSku());
        assertEquals("Erkek Lacivert Kemer L6149AZ.19SM.NV42", link.getName());
        assertEquals("25.99", link.getPrice().toString());
        assertEquals("Defacto", link.getBrand());
        assertEquals("DeFacto", link.getSeller());
        assertEquals("DeFacto tarafından gönderilecektir.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("3058612", link.getSku());
        assertEquals("Altus AL 6100 L A+++ 1000 Devir 6 kg Çamaşır Makinesi AL6100L", link.getName());
        assertEquals("1199.00", link.getPrice().toString());
        assertEquals("Altus", link.getBrand());
        assertEquals("Beyaz Live", link.getSeller());
        assertEquals("Kargo Bedava", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
