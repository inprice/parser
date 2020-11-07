package io.inprice.parser.websites.us;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Amazon_US_Test {

  private final String SITE_NAME = "amazon";
  private final String COUNTRY_CODE = "us";

  private final Website site = new io.inprice.parser.websites.xx.Amazon();

  @Test
  public void test_product_1() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B0759YSF4W", link.getSku());
    assertEquals("Samsung Chromebook 3, 11.6in, 4GB RAM, 16GB eMMC, Chromebook (XE500C13-K04US) (Renewed)",
        link.getName());
    assertEquals("142.90", link.getPrice().toString());
    assertEquals("Samsung", link.getBrand());
    assertEquals("Holiday Express(SN Recorded)", link.getSeller());
    assertEquals("This item ships to Turkey. Get it by Tuesday, June 18 Choose this date at checkout.",
        link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B0787V183F", link.getSku());
    assertEquals("Google Pixel 2 64 GB, Black Factory Unlocked (Renewed)", link.getName());
    assertEquals("294.99", link.getPrice().toString());
    assertEquals("Google", link.getBrand());
    assertEquals("toronto cellular", link.getSeller());
    assertEquals(
        "This item does not ship to Turkey. Please check other sellers who may ship internationally.",
        link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B07PGR2G13", link.getSku());
    assertEquals(
        "SAME DAY SHIPPING before 12pm Personalized Vertical Bar Necklace Coordinate Jewelry Mothers Day Gift Roman Numeral Graduation Gift Engraved 3D Necklaces for Women Initial Necklace - 4SBN",
        link.getName());
    assertEquals("16.58", link.getPrice().toString());
    assertEquals("Amazon", link.getBrand());
    assertEquals("MignonandMignon", link.getSeller());
    assertEquals("This item does not ship to Turkey. Please check other sellers who may ship internationally.",
        link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_4() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B00SMJHB6C", link.getSku());
    assertEquals("Clarks Men's Tilden Walk Oxford", link.getName());
    assertEquals("67.69", link.getPrice().toString());
    assertEquals("tilden walk clarks", link.getBrand());
    assertEquals("Amazon", link.getSeller());
    assertEquals(Consts.Words.NOT_AVAILABLE, link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_5() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B014I8T0YQ", link.getSku());
    assertEquals("AmazonBasics High-Speed HDMI Cable, 10 Feet, 1-Pack", link.getName());
    assertEquals("8.99", link.getPrice().toString());
    assertEquals("AmazonBasics", link.getBrand());
    assertEquals("Amazon", link.getSeller());
    assertEquals("This item ships to Turkey. Get it by Monday, June 22 - Wednesday, July 8 Choose this date at checkout.", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

}
