package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Argos_UK_Test {

    private final String SITE_NAME = "argos";
    private final String COUNTRY_CODE = "uk";

    private final Argos site = new Argos(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1344466", link.getSku());
        assertEquals("Amazon Echo Show 5 - Sandstone", link.getName());
        assertEquals("79.99", link.getPrice().toString());
        assertEquals("Amazon Echo", link.getBrand());
        assertEquals("Argos", link.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("3450925", link.getSku());
        assertEquals("3 Burner Propane Gas BBQ with Side Burner", link.getName());
        assertEquals("90.00", link.getPrice().toString());
        assertEquals("Unbranded", link.getBrand());
        assertEquals("Argos", link.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("3653351", link.getSku());
        assertEquals("Chad Valley Rectangular Paddling Pool -6ft-11in - 400 Litres", link.getName());
        assertEquals("18.00", link.getPrice().toString());
        assertEquals("Chad Valley", link.getBrand());
        assertEquals("Argos", link.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("8656793", link.getSku());
        assertEquals("SIM Free Huawei Mate 20 Pro 128GB Mobile - Twilight", link.getName());
        assertEquals("599.95", link.getPrice().toString());
        assertEquals("Huawei", link.getBrand());
        assertEquals("Argos", link.getSeller());
        assertEquals("In-store pickup OR Fast Track. Same day delivery. Only £3.95.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
