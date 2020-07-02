package io.inprice.parser.websites.it;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Lidl_IT_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.parser.websites.xx.Lidl(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("25951", competitor.getSku());
        assertEquals("Pettine elettrico lisciante", competitor.getName());
        assertEquals("14.99", competitor.getPrice().toString());
        assertEquals("Pettine", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("25959", competitor.getSku());
        assertEquals("Dispositivo per massaggi sottovuoto", competitor.getName());
        assertEquals("22.99", competitor.getPrice().toString());
        assertEquals("Dispositivo", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("25984", competitor.getSku());
        assertEquals("Cuscino 50x80 cm", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("Cuscino", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("26050", competitor.getSku());
        assertEquals("Robot aspirapolvere", competitor.getName());
        assertEquals("129.00", competitor.getPrice().toString());
        assertEquals("Robot", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
