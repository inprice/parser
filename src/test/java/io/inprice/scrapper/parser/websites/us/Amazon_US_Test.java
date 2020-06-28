package io.inprice.scrapper.parser.websites.us;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Amazon_US_Test {

  private final String SITE_NAME = "amazon";
  private final String COUNTRY_CODE = "us";

  private final Website site = new io.inprice.scrapper.parser.websites.xx.Amazon(new Competitor());

  @Test
  public void test_product_1() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B0759YSF4W", competitor.getSku());
    assertEquals("Samsung Chromebook 3, 11.6in, 4GB RAM, 16GB eMMC, Chromebook (XE500C13-K04US) (Renewed)",
        competitor.getName());
    assertEquals("142.90", competitor.getPrice().toString());
    assertEquals("Samsung", competitor.getBrand());
    assertEquals("Holiday Express(SN Recorded)", competitor.getSeller());
    assertEquals("This item ships to Turkey. Get it by Tuesday, June 18 Choose this date at checkout.",
        competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B0787V183F", competitor.getSku());
    assertEquals("Google Pixel 2 64 GB, Black Factory Unlocked (Renewed)", competitor.getName());
    assertEquals("294.99", competitor.getPrice().toString());
    assertEquals("Google", competitor.getBrand());
    assertEquals("toronto cellular", competitor.getSeller());
    assertEquals(
        "This item does not ship to Turkey. Please check other sellers who may ship internationally.",
        competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B07PGR2G13", competitor.getSku());
    assertEquals(
        "SAME DAY SHIPPING before 12pm Personalized Vertical Bar Necklace Coordinate Jewelry Mothers Day Gift Roman Numeral Graduation Gift Engraved 3D Necklaces for Women Initial Necklace - 4SBN",
        competitor.getName());
    assertEquals("16.58", competitor.getPrice().toString());
    assertEquals("Amazon", competitor.getBrand());
    assertEquals("MignonandMignon", competitor.getSeller());
    assertEquals("This item does not ship to Turkey. Please check other sellers who may ship internationally.",
        competitor.getShipment());
    assertNull(competitor.getSpecList());
  }

  @Test
  public void test_product_4() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B00SMJHB6C", competitor.getSku());
    assertEquals("Clarks Men's Tilden Walk Oxford", competitor.getName());
    assertEquals("67.69", competitor.getPrice().toString());
    assertEquals("Clarks", competitor.getBrand());
    assertEquals("Amazon", competitor.getSeller());
    assertEquals("NA", competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

  @Test
  public void test_product_5() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B014I8T0YQ", competitor.getSku());
    assertEquals("AmazonBasics High-Speed HDMI Cable, 10 Feet, 1-Pack", competitor.getName());
    assertEquals("8.99", competitor.getPrice().toString());
    assertEquals("AmazonBasics", competitor.getBrand());
    assertEquals("Amazon", competitor.getSeller());
    assertEquals("This item ships to Turkey. Get it by Monday, June 22 - Wednesday, July 8 Choose this date at checkout.", competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

}
