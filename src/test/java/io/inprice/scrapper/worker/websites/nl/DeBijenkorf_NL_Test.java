package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeBijenkorf_NL_Test {

    private final String SITE_NAME = "debijenkorf";
    private final String COUNTRY_CODE = "nl";

    private final DeBijenkorf site = new DeBijenkorf(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("3893011758", link.getSku());
        assertEquals("Swarovski Stone Signet ring 5412032", link.getName());
        assertEquals("64.50", link.getPrice().toString());
        assertEquals("Swarovski", link.getBrand());
        assertEquals("sieraden", link.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("4549010000", link.getSku());
        assertEquals("Balenciaga I Love Techno T-shirt met borduring", link.getName());
        assertEquals("279.30", link.getPrice().toString());
        assertEquals("Balenciaga", link.getBrand());
        assertEquals("herenmode", link.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("8678010071", link.getSku());
        assertEquals("Seafolly Splendour triangel bikinitop met bloemendessin", link.getName());
        assertEquals("47.20", link.getPrice().toString());
        assertEquals("Seafolly", link.getBrand());
        assertEquals("seafolly", link.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("7303090536", link.getSku());
        assertEquals("Godiva Gold Rigid Box assortiment bonbons 34 stuks", link.getName());
        assertEquals("48.95", link.getPrice().toString());
        assertEquals("Godiva", link.getBrand());
        assertEquals("wijn-delicatessen", link.getSeller());
        assertEquals("Voor 22.00 uur besteld, morgen in huis", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
