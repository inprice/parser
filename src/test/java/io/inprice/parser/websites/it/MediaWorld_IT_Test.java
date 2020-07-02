package io.inprice.parser.websites.it;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaWorld_IT_Test {

    private final String SITE_NAME = "mediaworld";
    private final String COUNTRY_CODE = "it";

    private final MediaWorld site = new MediaWorld(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("p-100094", competitor.getSku());
        assertEquals("NIKON D3500+AF-P 18/55VR+ZAINO BLACK (Fotocamera Reflex con Obiettivo)", competitor.getName());
        assertEquals("399.00", competitor.getPrice().toString());
        assertEquals("NIKON", competitor.getBrand());
        assertEquals("Media World", competitor.getSeller());
        assertEquals("A casa tua entro mercoledì 19 giugno Consegna Standard € 7.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("p-718631", competitor.getSku());
        assertEquals("AVENT SCD501/00 (Baby Monitor DECT Philips AVENT)", competitor.getName());
        assertEquals("52.99", competitor.getPrice().toString());
        assertEquals("AVENT", competitor.getBrand());
        assertEquals("Media World", competitor.getSeller());
        assertEquals("A casa tua entro mercoledì 19 giugno Consegna Standard € 4.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("p-736235", competitor.getSku());
        assertEquals("BOSCH SMZ5300 (Accessorio speciale per lavastoviglie)", competitor.getName());
        assertEquals("29.99", competitor.getPrice().toString());
        assertEquals("BOSCH", competitor.getBrand());
        assertEquals("Media World", competitor.getSeller());
        assertEquals("A casa tua entro giovedì 04 luglio Consegna Standard € 4.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("p-641394", competitor.getSku());
        assertEquals("GIGASET DA710 Black (Telefoni con Filo)", competitor.getName());
        assertEquals("42.99", competitor.getPrice().toString());
        assertEquals("GIGASET", competitor.getBrand());
        assertEquals("Media World", competitor.getSeller());
        assertEquals("A casa tua entro mercoledì 19 giugno Consegna Standard € 4.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
