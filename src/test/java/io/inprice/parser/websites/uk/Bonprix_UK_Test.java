package io.inprice.parser.websites.uk;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_UK_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.parser.websites.xx.Bonprix(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("936861-128-Black/White", link.getSku());
        assertEquals("Suit & Shirt & Tie by bpc bonprix collection | bonprix", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("960056-12-BlackPrint", link.getSku());
        assertEquals("Printed Wrap Tankini by bpc selection | bonprix", link.getName());
        assertEquals("37.99", link.getPrice().toString());
        assertEquals("bpc selection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("948669-12-BerryMarl", link.getSku());
        assertEquals("Soft Shell Parka by bpc bonprix collection | bonprix", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("912263-1-GreyStripe", link.getSku());
        assertEquals("Tie & Pocket Square by bpc selection | bonprix", link.getName());
        assertEquals("12.99", link.getPrice().toString());
        assertEquals("bpc selection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("3.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
