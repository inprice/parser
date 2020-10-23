package io.inprice.parser.websites.nl;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lidl_NL_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.parser.websites.xx.Lidl();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("19356", link.getSku());
        assertEquals("Heren sportshort", link.getName());
        assertEquals("5.99", link.getPrice().toString());
        assertEquals("Lidl.nl", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("19352", link.getSku());
        assertEquals("Activity-tracker", link.getName());
        assertEquals("39.99", link.getPrice().toString());
        assertEquals("Lidl.nl", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("19419", link.getSku());
        assertEquals("Jersey hoeslaken", link.getName());
        assertEquals("8.99", link.getPrice().toString());
        assertEquals("Lidl.nl", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("100257246", link.getSku());
        assertEquals("FLORABEST Relaxstoel", link.getName());
        assertEquals("39.99", link.getPrice().toString());
        assertEquals("", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Eerste levering mogelijk vanaf 21-06-2019. Beschikbaarheid De verkoop van de artikelen uit " +
                "de folder start in de filialen op de aangegeven actiedag. O", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
