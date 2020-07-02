package io.inprice.parser.websites.au;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_AU_Test {

  private final String SITE_NAME = "amazon";
  private final String COUNTRY_CODE = "au";

  private final Website site = new io.inprice.parser.websites.xx.Amazon(new Competitor());

  @Test
  public void test_product_1() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B07FTHCPDP", competitor.getSku());
    assertEquals("Antler 4227130015 Juno 2 4W Large Roller Case Suitcases (Hardside), Turquoise, 81 cm",
        competitor.getName());
    assertEquals("179.00", competitor.getPrice().toString());
    assertEquals("ANTLER", competitor.getBrand());
    assertEquals("Bags_To_Go", competitor.getSeller());
    assertEquals("+ FREE Delivery", competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B072KMX18S", competitor.getSku());
    assertEquals("Emporio Armani Men's Quartz Watch Analog Display and Stainless Steel Strap, AR11068",
        competitor.getName());
    assertEquals("233.00", competitor.getPrice().toString());
    assertEquals("Emporio Armani", competitor.getBrand());
    assertEquals("Amazon AU", competitor.getSeller());
    assertEquals("& FREE Delivery, plus Free Returns See details and conditions", competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B0755PDKCN", competitor.getSku());
    assertEquals("GoPro The Handler 2017 Version DVC Accessories, Orange", competitor.getName());
    assertEquals("46.34", competitor.getPrice().toString());
    assertEquals("GoPro", competitor.getBrand());
    assertEquals("Amazon US", competitor.getSeller());
    assertEquals("+ FREE Delivery", competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

    assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
    assertEquals("B07G9ZGL9X", competitor.getSku());
    assertEquals("M&M's Milk Chocolate Party Size Bucket (640g) (Packaging may vary)", competitor.getName());
    assertEquals("9.28", competitor.getPrice().toString());
    assertEquals("M&M'S", competitor.getBrand());
    assertEquals("Amazon AU", competitor.getSeller());
    assertEquals("& Free Delivery on eligible orders See details and conditions", competitor.getShipment());
    assertTrue(competitor.getSpecList().size() > 0);
  }

}
