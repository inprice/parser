package io.inprice.parser.websites.de;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Otto_DE_Test {

    private final String SITE_NAME = "otto";
    private final String COUNTRY_CODE = "de";

    private final Otto site = new Otto(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3826274584", competitor.getSku());
        assertEquals("KangaROOS Jeansrock in modischer Moonwashed-Optik", competitor.getName());
        assertEquals("59.99", competitor.getPrice().toString());
        assertEquals("KangaROOS", competitor.getBrand());
        assertEquals("Otto", competitor.getSeller());
        assertEquals("lieferbar - in  4-6 Werktagen bei dir", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("67398585", competitor.getSku());
        assertEquals("H.I.S Freizeitsocken (10 Paar)", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("H.I.S", competitor.getBrand());
        assertEquals("Otto", competitor.getSeller());
        assertEquals("lieferbar - in  4-6 Werktagen bei dir", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("350453A", competitor.getSku());
        assertEquals("KARIBU Aufgusskonzentrat »Lemongras«, 250 ml", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("Karibu", competitor.getBrand());
        assertEquals("Otto", competitor.getSeller());
        assertEquals("lieferbar in 2 Wochen", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("5316230027", competitor.getSku());
        assertEquals("LG Side-by-Side GSL361ICEZ, 179 cm hoch, 91,2 cm breit", competitor.getName());
        assertEquals("1099.00", competitor.getPrice().toString());
        assertEquals("LG", competitor.getBrand());
        assertEquals("Otto", competitor.getSeller());
        assertEquals("lieferbar - in 2-3 Werktagen bei dir", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
