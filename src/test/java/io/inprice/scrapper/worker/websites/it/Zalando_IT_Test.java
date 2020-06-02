package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_IT_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("F1451A005-K11", competitor.getSku());
        assertEquals("STUDDED GLOVES - Guanti", competitor.getName());
        assertEquals("103.99", competitor.getPrice().toString());
        assertEquals("Filippa K", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Consegna standard: Gratis entro 3-6 giorni lavorativi Consegna express: 7,95€ 1-3 giorni lavorativi", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("M0Q21D0BE-Q11", competitor.getSku());
        assertEquals("SHEARED BANDEAU 2 PACK  - Top", competitor.getName());
        assertEquals("14.69", competitor.getPrice().toString());
        assertEquals("Missguided", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Consegna standard: Gratis entro 3-6 giorni lavorativi Consegna express: 7,95€ 1-3 giorni lavorativi", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("APC31F002-G16", competitor.getSku());
        assertEquals("WATER LIGHT TINT - Tinta labbra", competitor.getName());
        assertEquals("8.99", competitor.getPrice().toString());
        assertEquals("A'PIEU", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Consegna standard: Gratis entro 3-6 giorni lavorativi Consegna express: 7,95€ 1-3 giorni lavorativi", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("4SW51L0B2-F11", competitor.getSku());
        assertEquals("VINTAGE PENDANT PEAR - Collana", competitor.getName());
        assertEquals("79.99", competitor.getPrice().toString());
        assertEquals("Swarovski", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Consegna standard: Gratis entro 3-6 giorni lavorativi Consegna express: 7,95€ 1-3 giorni lavorativi", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
