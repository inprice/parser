package io.inprice.scrapper.worker.websites.nl;

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
public class Lidl_NL_Test {

    private final String SITE_NAME = "lidl";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Lidl(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
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

        assertEquals(Status.ACTIVE, link.getStatus());
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

        assertEquals(Status.ACTIVE, link.getStatus());
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

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("100257246", link.getSku());
        assertEquals("FLORABEST® Relaxstoel", link.getName());
        assertEquals("39.99", link.getPrice().toString());
        assertEquals("", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("Eerste levering mogelijk vanaf 21-06-2019. Beschikbaarheid De verkoop van de artikelen uit " +
                "de folder start in de filialen op de aangegeven actiedag. Online verkrijgbare actieartikelen kunnen eerder " +
                "besteld worden, maar worden pas uitgeleverd vanaf de eerste actiedag. Vragen over artikel beschikbaarheid en actuele aanbiedingen ?", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
