package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Lidl_DE_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("000000000100234276", link.getSku());
        assertEquals("Hoover Waschmaschine HL4 1472D3/1-S", link.getName());
        assertEquals("319.00", link.getPrice().toString());
        assertEquals("Hoover", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Innerhalb von 5 Werktagen", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("000000000100239698", link.getSku());
        assertEquals("DENVER Bluetooth Smartwatch SW-500", link.getName());
        assertEquals("99.99", link.getPrice().toString());
        assertEquals("DENVER", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Innerhalb von 3 Werktagen", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("000000000100203237", link.getSku());
        assertEquals("BIG Spielfahrzeug Power Worker Radlader", link.getName());
        assertEquals("15.99", link.getPrice().toString());
        assertEquals("BIG", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Innerhalb von 3 Werktagen", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("000000000100031233", link.getSku());
        assertEquals("Christopeit Total Exerciser TE 1", link.getName());
        assertEquals("115.00", link.getPrice().toString());
        assertEquals("Christopeit", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Innerhalb von 3 Werktagen", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
