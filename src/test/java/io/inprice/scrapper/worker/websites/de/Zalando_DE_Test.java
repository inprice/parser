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
public class Zalando_DE_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("TIB31H01D-S11", link.getSku());
        assertEquals("BED HEAD AFTER PARTY 100ML - Styling", link.getName());
        assertEquals("16.05", link.getPrice().toString());
        assertEquals("Tigi", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("ES183C02R-K11", link.getSku());
        assertEquals("BRAVA BEACH AMERICAN NECKHOLDER HIPSTER - Bikini", link.getName());
        assertEquals("27.95", link.getPrice().toString());
        assertEquals("Esprit", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("GU152M00V-D11", link.getSku());
        assertEquals("MENS SPORT - Uhr - silver", link.getName());
        assertEquals("189.95", link.getPrice().toString());
        assertEquals("Guess", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("K4451K00T-Q11", link.getSku());
        assertEquals("Sonnenbrille", link.getName());
        assertEquals("12.75", link.getPrice().toString());
        assertEquals("KIOMI", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
