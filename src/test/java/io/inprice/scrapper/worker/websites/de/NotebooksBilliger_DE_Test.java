package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NotebooksBilliger_DE_Test {

    private final String SITE_NAME = "notebooksbilliger";
    private final String COUNTRY_CODE = "de";

    private final NotebooksBilliger site = new NotebooksBilliger(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("A 668178", competitor.getSku());
        assertEquals("HP Active Pen mit N-Trig Technologie [für Spectre / Pavilion / Envy Modelle]", competitor.getName());
        assertEquals("49.99", competitor.getPrice().toString());
        assertEquals("HP", competitor.getBrand());
        assertEquals("Notebooks Billiger", competitor.getSeller());
        assertEquals("Abholung im Geschäft", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("A 746272", competitor.getSku());
        assertEquals("Adobe Premiere Elements 2019 [PC/Mac] [Vollversion]", competitor.getName());
        assertEquals("72.99", competitor.getPrice().toString());
        assertEquals("Adobe", competitor.getBrand());
        assertEquals("Notebooks Billiger", competitor.getSeller());
        assertEquals("Abholung im Geschäft", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("A 773701", competitor.getSku());
        assertEquals("Asus VivoBook S13 S330FA-EY005T / 13,3\" FHD / Intel Core i5-8265U / 8GB RAM / 256GB SSD / Windows 10", competitor.getName());
        assertEquals("649.00", competitor.getPrice().toString());
        assertEquals("ASUS", competitor.getBrand());
        assertEquals("Notebooks Billiger", competitor.getSeller());
        assertEquals("Abholung im Geschäft", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("A 748424", competitor.getSku());
        assertEquals("Trust GXT 307 Ravu Gaming Headset, kabelgebunden, Over-Ear-Design", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("TRUST", competitor.getBrand());
        assertEquals("Notebooks Billiger", competitor.getSeller());
        assertEquals("Abholung im Geschäft", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
