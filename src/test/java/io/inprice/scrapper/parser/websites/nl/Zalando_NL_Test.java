package io.inprice.scrapper.parser.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_NL_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.Zalando(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("BJ282H006-K12", competitor.getSku());
        assertEquals("SANDRO SWIM - Zwemshorts", competitor.getName());
        assertEquals("27.45", competitor.getPrice().toString());
        assertEquals("Björn Borg", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("MQ581F008-O11", competitor.getSku());
        assertEquals("SUMMER - Panty", competitor.getName());
        assertEquals("14.95", competitor.getPrice().toString());
        assertEquals("MAGIC Bodyfashion", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("VE052L00M-F11", competitor.getSku());
        assertEquals("Ring - gold", competitor.getName());
        assertEquals("103.95", competitor.getPrice().toString());
        assertEquals("Versus Versace", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1MI22P01E-K11", competitor.getSku());
        assertEquals("NEW LOGO - Poloshirt", competitor.getName());
        assertEquals("89.95", competitor.getPrice().toString());
        assertEquals("Michael Kors", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
