package io.inprice.scrapper.parser.websites.tr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaMarkt_TR_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "tr";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.MediaMarkt(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1112117", competitor.getSku());
        assertEquals("GIGASET A415 Dect Telefon", competitor.getName());
        assertEquals("199.00", competitor.getPrice().toString());
        assertEquals("GIGASET", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Kargo Ücreti 7,90", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1170313", competitor.getSku());
        assertEquals("TRUST URBAN Trust Urban 21711 12W USB Hızlı Araç Şarj Cihazı", competitor.getName());
        assertEquals("59.99", competitor.getPrice().toString());
        assertEquals("TRUST URBAN", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Kargo Ücreti 7,90", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1109421", competitor.getSku());
        assertEquals("FRISBY FNC 37ST Laptop Soğutucu", competitor.getName());
        assertEquals("69.99", competitor.getPrice().toString());
        assertEquals("FRISBY", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Kargo Ücreti 7,90", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1181169", competitor.getSku());
        assertEquals("WAHL Burun Kılı Kesme-Pilli", competitor.getName());
        assertEquals("29.99", competitor.getPrice().toString());
        assertEquals("WAHL", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Kargo Ücreti 7,90", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
