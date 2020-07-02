package io.inprice.parser.websites.it;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Euronics_IT_Test {

    private final String SITE_NAME = "euronics";
    private final String COUNTRY_CODE = "it";

    private final Euronics site = new Euronics(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("eProd182001118", competitor.getSku());
        assertEquals("ASUS - CERBERUS V2/RED - Nero/Rosso", competitor.getName());
        assertEquals("0.00", competitor.getPrice().toString());
        assertEquals("ASUS", competitor.getBrand());
        assertEquals("Euronics", competitor.getSeller());
        assertEquals("NA", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("eProd182001461", competitor.getSku());
        assertEquals("OLIMPIA SPLENDID - Peler 4E - Bianco", competitor.getName());
        assertEquals("79.99", competitor.getPrice().toString());
        assertEquals("OLIMPIA SPLENDID", competitor.getBrand());
        assertEquals("Euronics", competitor.getSeller());
        assertEquals("Consegna €11", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("eProd192005354", competitor.getSku());
        assertEquals("SAMSUNG - RB41R7719S9/EF - metal inox", competitor.getName());
        assertEquals("899.00", competitor.getPrice().toString());
        assertEquals("SAMSUNG", competitor.getBrand());
        assertEquals("Euronics", competitor.getSeller());
        assertEquals("Consegna al piano €77", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("eProd192003278", competitor.getSku());
        assertEquals("XIAOMI - Redmi Note 7 4+64 - Blu", competitor.getName());
        assertEquals("199.00", competitor.getPrice().toString());
        assertEquals("XIAOMI", competitor.getBrand());
        assertEquals("Euronics", competitor.getSeller());
        assertEquals("Consegna €11", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
