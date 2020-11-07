package io.inprice.parser.websites.de;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NotebooksBilliger_DE_Test {

    private final String SITE_NAME = "notebooksbilliger";
    private final String COUNTRY_CODE = "de";

    private final NotebooksBilliger site = new NotebooksBilliger();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("A 668178", link.getSku());
        assertEquals("HP Active Pen mit N-Trig Technologie [für Spectre / Pavilion / Envy Modelle]", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("HP", link.getBrand());
        assertEquals("Notebooks Billiger", link.getSeller());
        assertEquals("Abholung im Geschäft", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("A 746272", link.getSku());
        assertEquals("Adobe Premiere Elements 2019 [PC/Mac] [Vollversion]", link.getName());
        assertEquals("72.99", link.getPrice().toString());
        assertEquals("Adobe", link.getBrand());
        assertEquals("Notebooks Billiger", link.getSeller());
        assertEquals("Abholung im Geschäft", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("A 773701", link.getSku());
        assertEquals("Asus VivoBook S13 S330FA-EY005T / 13,3\" FHD / Intel Core i5-8265U / 8GB RAM / 256GB SSD / Windows 10", link.getName());
        assertEquals("649.00", link.getPrice().toString());
        assertEquals("ASUS", link.getBrand());
        assertEquals("Notebooks Billiger", link.getSeller());
        assertEquals("Abholung im Geschäft", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("A 748424", link.getSku());
        assertEquals("Trust GXT 307 Ravu Gaming Headset, kabelgebunden, Over-Ear-Design", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("TRUST", link.getBrand());
        assertEquals("Notebooks Billiger", link.getSeller());
        assertEquals("Abholung im Geschäft", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
