package io.inprice.parser.websites.uk;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Debenhams_UK_Test {

    private final String SITE_NAME = "debenhams";
    private final String COUNTRY_CODE = "uk";

    private final Debenhams site = new Debenhams();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("3310014132", link.getSku());
        assertEquals("Black non-stick aluminium 'Gourmet' 5 piece pan set", link.getName());
        assertEquals("68.00", link.getPrice().toString());
        assertEquals("Tefal", link.getBrand());
        assertEquals("Debenhams", link.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1610104181", link.getSku());
        assertEquals("Black Embroidered Mesh Satin Brazilian Knickers", link.getName());
        assertEquals("10.00", link.getPrice().toString());
        assertEquals("Reger by Janet Reger", link.getBrand());
        assertEquals("Debenhams", link.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("61131_105548", link.getSku());
        assertEquals("Pleasure Dome Topkapi Opal Designer Wallpaper", link.getName());
        assertEquals("12.50", link.getPrice().toString());
        assertEquals("Laurence Llewelyn-Bowen", link.getBrand());
        assertEquals("Debenhams", link.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1710104267", link.getSku());
        assertEquals("My First Talking Ted", link.getName());
        assertEquals("8.80", link.getPrice().toString());
        assertEquals("Early Learning Centre", link.getBrand());
        assertEquals("Debenhams", link.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
