package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Wehkamp_NL_Test {

    private final String SITE_NAME = "wehkamp";
    private final String COUNTRY_CODE = "nl";

    private final Wehkamp site = new Wehkamp(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("902551", link.getSku());
        assertEquals("POWXG60225 benzine grasmaaier", link.getName());
        assertEquals("438.95", link.getPrice().toString());
        assertEquals("Powerplus", link.getBrand());
        assertEquals("wehkamp", link.getSeller());
        assertEquals("gratis bezorging en retour", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("533816", link.getSku());
        assertEquals("Schommelstoel Montmartre", link.getName());
        assertEquals("65.00", link.getPrice().toString());
        assertEquals("whkmp's own", link.getBrand());
        assertEquals("wehkamp", link.getSeller());
        assertEquals("gratis bezorging en retour", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("900893", link.getSku());
        assertEquals("Ride-On MEGA zwaan (194 cm)", link.getName());
        assertEquals("33.95", link.getPrice().toString());
        assertEquals("Intex", link.getBrand());
        assertEquals("wehkamp", link.getSeller());
        assertEquals("gratis bezorging en retour", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("16218807", link.getSku());
        assertEquals("sandalen blauw", link.getName());
        assertEquals("29.95", link.getPrice().toString());
        assertEquals("Braqeez", link.getBrand());
        assertEquals("wehkamp", link.getSeller());
        assertEquals("gratis bezorging en retour", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}