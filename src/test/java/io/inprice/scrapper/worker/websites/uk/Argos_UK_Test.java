package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Argos_UK_Test {

    private final String SITE_NAME = "argos";
    private final String COUNTRY_CODE = "uk";

    private final Argos site = new Argos(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1344466", competitor.getSku());
        assertEquals("Amazon Echo Show 5 - Sandstone", competitor.getName());
        assertEquals("79.99", competitor.getPrice().toString());
        assertEquals("Amazon Echo", competitor.getBrand());
        assertEquals("Argos", competitor.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3450925", competitor.getSku());
        assertEquals("3 Burner Propane Gas BBQ with Side Burner", competitor.getName());
        assertEquals("90.00", competitor.getPrice().toString());
        assertEquals("Unbranded", competitor.getBrand());
        assertEquals("Argos", competitor.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3653351", competitor.getSku());
        assertEquals("Chad Valley Rectangular Paddling Pool -6ft-11in - 400 Litres", competitor.getName());
        assertEquals("18.00", competitor.getPrice().toString());
        assertEquals("Chad Valley", competitor.getBrand());
        assertEquals("Argos", competitor.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("8656793", competitor.getSku());
        assertEquals("SIM Free Huawei Mate 20 Pro 128GB Mobile - Twilight", competitor.getName());
        assertEquals("599.95", competitor.getPrice().toString());
        assertEquals("Huawei", competitor.getBrand());
        assertEquals("Argos", competitor.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
