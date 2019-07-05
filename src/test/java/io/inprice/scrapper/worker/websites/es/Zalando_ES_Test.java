package io.inprice.scrapper.worker.websites.es;

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
public class Zalando_ES_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("C1852F00Z-Q11", link.getSku());
        assertEquals("Monedero", link.getName());
        assertEquals("38.95", link.getPrice().toString());
        assertEquals("Calvin Klein", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("MQ581F008-O11", link.getSku());
        assertEquals("SUMMER - Panty", link.getName());
        assertEquals("14.95", link.getPrice().toString());
        assertEquals("MAGIC Bodyfashion", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("VE052L00M-F11", link.getSku());
        assertEquals("Ring - gold", link.getName());
        assertEquals("103.95", link.getPrice().toString());
        assertEquals("Versus Versace", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1MI22P01E-K11", link.getSku());
        assertEquals("NEW LOGO - Poloshirt", link.getName());
        assertEquals("89.95", link.getPrice().toString());
        assertEquals("Michael Kors", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard shipment", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
