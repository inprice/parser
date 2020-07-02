package io.inprice.parser.websites.es;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaMarkt_ES_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.parser.websites.xx.MediaMarkt(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("1299751", competitor.getSku());
        assertEquals("Memoria USB 64 GB - Sandisk 139789 Ultra Flair, USB 3.0, Velocidad hasta 150mb/sg", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("SANDISK", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("más envío 1,99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1272050", competitor.getSku());
        assertEquals("Disco duro de 2TB - WD Elements, 2.5 pulgadas", competitor.getName());
        assertEquals("68.00", competitor.getPrice().toString());
        assertEquals("WESTERN DIGITAL", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("más envío 1,99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1275902", competitor.getSku());
        assertEquals("Cafetera - De Longhi EC7.1 Potencia 800W, Sistema Cappuccino, Tapón de seguridad", competitor.getName());
        assertEquals("64.90", competitor.getPrice().toString());
        assertEquals("DE LONGHI", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("más envío 2,99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("1405935", competitor.getSku());
        assertEquals("PS4 Marvel S Spider-Man", competitor.getName());
        assertEquals("24.90", competitor.getPrice().toString());
        assertEquals("SONY COMPUTER ENT. S.A. (SOFT)", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("más envío 1,99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
