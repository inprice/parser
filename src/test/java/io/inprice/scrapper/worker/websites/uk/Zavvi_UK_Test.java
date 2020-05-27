package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zavvi_UK_Test {

    private final String SITE_NAME = "zavvi";
    private final String COUNTRY_CODE = "uk";

    private final Zavvi site = new Zavvi(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("11852041", link.getSku());
        assertEquals("Meta Merch Star Wars Chewbacca Arm Mug", link.getName());
        assertEquals("7.99", link.getPrice().toString());
        assertEquals("Exquisite Gaming", link.getBrand());
        assertEquals("Zavvi", link.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
        assertEquals("12071095", link.getSku());
        assertEquals("Marvel Pop! Advent Calendar (2019)", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("Funko Pop! Vinyl", link.getBrand());
        assertEquals("Zavvi", link.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("11477837", link.getSku());
        assertEquals("Nintendo Retro NES Classically Trained Men's White T-Shirt", link.getName());
        assertEquals("14.99", link.getPrice().toString());
        assertEquals("Nintendo", link.getBrand());
        assertEquals("Zavvi", link.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
        assertEquals("12183579", link.getSku());
        assertEquals("Sonic the Hedgehog BOOM8 Series PVC Figure Vol. 02 Sonic (8cm)", link.getName());
        assertEquals("24.99", link.getPrice().toString());
        assertEquals("First 4 Figures", link.getBrand());
        assertEquals("Zavvi", link.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
