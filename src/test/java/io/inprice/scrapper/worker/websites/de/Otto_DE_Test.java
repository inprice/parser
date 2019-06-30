package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Otto_DE_Test {

    private final String SITE_NAME = "otto";
    private final String COUNTRY_CODE = "de";

    private final Otto site = new Otto(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("3826274584", link.getSku());
        assertEquals("KangaROOS Jeansrock in modischer Moonwashed-Optik", link.getName());
        assertEquals("59.99", link.getPrice().toString());
        assertEquals("KangaROOS", link.getBrand());
        assertEquals("Otto", link.getSeller());
        assertEquals("lieferbar - in  4-6 Werktagen bei dir", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("67398585", link.getSku());
        assertEquals("H.I.S Freizeitsocken (10 Paar)", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("H.I.S", link.getBrand());
        assertEquals("Otto", link.getSeller());
        assertEquals("lieferbar - in  4-6 Werktagen bei dir", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("350453A", link.getSku());
        assertEquals("KARIBU Aufgusskonzentrat »Lemongras«, 250 ml", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("Karibu", link.getBrand());
        assertEquals("Otto", link.getSeller());
        assertEquals("lieferbar in 2 Wochen", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("5316230027", link.getSku());
        assertEquals("LG Side-by-Side GSL361ICEZ, 179 cm hoch, 91,2 cm breit", link.getName());
        assertEquals("1099.00", link.getPrice().toString());
        assertEquals("LG", link.getBrand());
        assertEquals("Otto", link.getSeller());
        assertEquals("lieferbar - in 2-3 Werktagen bei dir", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}