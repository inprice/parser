package io.inprice.parser.websites.uk;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NewLook_UK_Test {

    private final String SITE_NAME = "newlook";
    private final String COUNTRY_CODE = "uk";

    private final NewLook site = new NewLook();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("623032293", link.getSku());
        assertEquals("Gold Wood Money Box and frame", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("New Look", link.getBrand());
        assertEquals("NewLook", link.getSeller());
        assertEquals("Free Delivery*", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("629261276", link.getSku());
        assertEquals("Bright Pink Satin Tiger Jacquard Midi Dress", link.getName());
        assertEquals("27.99", link.getPrice().toString());
        assertEquals("New Look", link.getBrand());
        assertEquals("NewLook", link.getSeller());
        assertEquals("Free Delivery*", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("616980110", link.getSku());
        assertEquals("White Side Stripe Lace Up Trainers", link.getName());
        assertEquals("11.24", link.getPrice().toString());
        assertEquals("New Look", link.getBrand());
        assertEquals("NewLook", link.getSeller());
        assertEquals("Free Delivery*", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("612265801", link.getSku());
        assertEquals("Black Leather-Look Chain Strap Utility Bum Bag", link.getName());
        assertEquals("12.99", link.getPrice().toString());
        assertEquals("New Look", link.getBrand());
        assertEquals("NewLook", link.getSeller());
        assertEquals("Free Delivery*", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
