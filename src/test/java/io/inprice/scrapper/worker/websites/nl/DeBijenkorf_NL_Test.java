package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeBijenkorf_NL_Test {

    private final String SITE_NAME = "debijenkorf";
    private final String COUNTRY_CODE = "nl";

    private final DeBijenkorf site = new DeBijenkorf(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3893011758", competitor.getSku());
        assertEquals("Swarovski Stone Signet ring 5412032", competitor.getName());
        assertEquals("64.50", competitor.getPrice().toString());
        assertEquals("Swarovski", competitor.getBrand());
        assertEquals("sieraden", competitor.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("4549010000", competitor.getSku());
        assertEquals("Balenciaga I Love Techno T-shirt met borduring", competitor.getName());
        assertEquals("279.30", competitor.getPrice().toString());
        assertEquals("Balenciaga", competitor.getBrand());
        assertEquals("herenmode", competitor.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("8678010071", competitor.getSku());
        assertEquals("Seafolly Splendour triangel bikinitop met bloemendessin", competitor.getName());
        assertEquals("47.20", competitor.getPrice().toString());
        assertEquals("Seafolly", competitor.getBrand());
        assertEquals("seafolly", competitor.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("7303090536", competitor.getSku());
        assertEquals("Godiva Gold Rigid Box assortiment bonbons 34 stuks", competitor.getName());
        assertEquals("48.95", competitor.getPrice().toString());
        assertEquals("Godiva", competitor.getBrand());
        assertEquals("wijn-delicatessen", competitor.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
