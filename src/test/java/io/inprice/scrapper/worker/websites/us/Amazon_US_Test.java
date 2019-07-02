package io.inprice.scrapper.worker.websites.us;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class Amazon_US_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "us";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B0759YSF4W", link.getSku());
        assertEquals("Samsung Chromebook 3, 11.6in, 4GB RAM, 16GB eMMC, Chromebook (XE500C13-K04US) (Renewed)", link.getName());
        assertEquals("142.90", link.getPrice().toString());
        assertEquals("Samsung", link.getBrand());
        assertEquals("Holiday Express(SN Recorded) ✅", link.getSeller());
        assertEquals("This item ships to Turkey. Get it by Tuesday, June 18 Choose this date at checkout. Learn more", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B0787V183F", link.getSku());
        assertEquals("Google Pixel 2 64 GB, Black Factory Unlocked (Renewed)", link.getName());
        assertEquals("294.99", link.getPrice().toString());
        assertEquals("Google", link.getBrand());
        assertEquals("toronto cellular", link.getSeller());
        assertEquals("This item does not ship to Turkey. Please check other sellers who may ship internationally. Learn more", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07PGR2G13", link.getSku());
        assertEquals("SAME DAY SHIPPING before 12pm Personalized Vertical Bar Necklace Coordinate Jewelry Mothers Day Gift Roman Numeral Graduation Gift Engraved 3D Necklaces for Women Initial Necklace - 4SBN", link.getName());
        assertEquals("16.58", link.getPrice().toString());
        assertEquals("Amazon", link.getBrand());
        assertEquals("MignonandMignon", link.getSeller());
        assertEquals("This item does not ship to Turkey. Please check other sellers who may ship internationally.", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B00SMJHB6C", link.getSku());
        assertEquals("Clarks Men's Tilden Walk Oxford", link.getName());
        assertEquals("67.69", link.getPrice().toString());
        assertEquals("tilden walk clarks", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("NA", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
