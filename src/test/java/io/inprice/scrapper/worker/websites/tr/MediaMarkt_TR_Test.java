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
public class MediaMarkt_TR_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "tr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.MediaMarkt(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1112117", link.getSku());
        assertEquals("GIGASET A415 Dect Telefon", link.getName());
        assertEquals("199.00", link.getPrice().toString());
        assertEquals("GIGASET", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Kargo Ücreti 7,90", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1170313", link.getSku());
        assertEquals("TRUST URBAN Trust Urban 21711 12W USB Hızlı Araç Şarj Cihazı", link.getName());
        assertEquals("59.99", link.getPrice().toString());
        assertEquals("TRUST URBAN", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Kargo Ücreti 7,90", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1109421", link.getSku());
        assertEquals("FRISBY FNC 37ST Laptop Soğutucu", link.getName());
        assertEquals("69.99", link.getPrice().toString());
        assertEquals("FRISBY", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Kargo Ücreti 7,90", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1181169", link.getSku());
        assertEquals("WAHL Burun Kılı Kesme-Pilli", link.getName());
        assertEquals("29.99", link.getPrice().toString());
        assertEquals("WAHL", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Kargo Ücreti 7,90", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
