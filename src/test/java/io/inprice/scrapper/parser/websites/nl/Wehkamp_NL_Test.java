package io.inprice.scrapper.parser.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Wehkamp_NL_Test {

    private final String SITE_NAME = "wehkamp";
    private final String COUNTRY_CODE = "nl";

    private final Wehkamp site = new Wehkamp(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("902551", competitor.getSku());
        assertEquals("POWXG60225 benzine grasmaaier", competitor.getName());
        assertEquals("438.95", competitor.getPrice().toString());
        assertEquals("Powerplus", competitor.getBrand());
        assertEquals("wehkamp", competitor.getSeller());
        assertEquals("gratis bezorging en retour", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("533816", competitor.getSku());
        assertEquals("Schommelstoel Montmartre", competitor.getName());
        assertEquals("65.00", competitor.getPrice().toString());
        assertEquals("whkmp's own", competitor.getBrand());
        assertEquals("wehkamp", competitor.getSeller());
        assertEquals("gratis bezorging en retour", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("900893", competitor.getSku());
        assertEquals("Ride-On MEGA zwaan (194 cm)", competitor.getName());
        assertEquals("33.95", competitor.getPrice().toString());
        assertEquals("Intex", competitor.getBrand());
        assertEquals("wehkamp", competitor.getSeller());
        assertEquals("gratis bezorging en retour", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("16218807", competitor.getSku());
        assertEquals("sandalen blauw", competitor.getName());
        assertEquals("29.95", competitor.getPrice().toString());
        assertEquals("Braqeez", competitor.getBrand());
        assertEquals("wehkamp", competitor.getSeller());
        assertEquals("gratis bezorging en retour", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
