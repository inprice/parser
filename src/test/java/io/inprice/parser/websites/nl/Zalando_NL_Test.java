package io.inprice.parser.websites.nl;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_NL_Test {

    private final Website site = new ZalandoNL();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(site, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("BJ282H006-K12", link.getSku());
        assertEquals("SANDRO SWIM - Zwemshorts", link.getName());
        assertEquals("27.45", link.getPrice().toString());
        assertEquals("Björn Borg", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(site, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("MQ581F008-O11", link.getSku());
        assertEquals("SUMMER - Panty", link.getName());
        assertEquals("14.95", link.getPrice().toString());
        assertEquals("MAGIC Bodyfashion", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(site, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("VE052L00M-F11", link.getSku());
        assertEquals("Ring - gold", link.getName());
        assertEquals("103.95", link.getPrice().toString());
        assertEquals("Versus Versace", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(site, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1MI22P01E-K11", link.getSku());
        assertEquals("NEW LOGO - Poloshirt", link.getName());
        assertEquals("89.95", link.getPrice().toString());
        assertEquals("Michael Kors", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standaard levering gratis 2-5 werkdagen Express € 9,95 Levering beschikbaar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
