package io.inprice.parser.websites.au;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.xx.Amazon;

public class Amazon_AU_Test {

  private final Website site = new Amazon() {};

  @Test
  public void test_product_1() {
    Link link = site.test(Helpers.getHtmlPath(site, 1));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B07FTHCPDP", link.getSku());
    assertEquals("Antler 4227130015 Juno 2 4W Large Roller Case Suitcases (Hardside), Turquoise, 81 cm",
        link.getName());
    assertEquals("179.00", link.getPrice().toString());
    assertEquals("ANTLER", link.getBrand());
    assertEquals("Bags_To_Go", link.getSeller());
    assertEquals("+ FREE Delivery", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    Link link = site.test(Helpers.getHtmlPath(site, 2));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B072KMX18S", link.getSku());
    assertEquals("Emporio Armani Men's Quartz Watch Analog Display and Stainless Steel Strap, AR11068", link.getName());
    assertEquals("233.00", link.getPrice().toString());
    assertEquals("Emporio Armani", link.getBrand());
    assertEquals("Amazon AU", link.getSeller());
    assertEquals("& FREE Delivery, plus Free Returns See details and conditions", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    Link link = site.test(Helpers.getHtmlPath(site, 3));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B0755PDKCN", link.getSku());
    assertEquals("GoPro The Handler 2017 Version DVC Accessories, Orange", link.getName());
    assertEquals("46.34", link.getPrice().toString());
    assertEquals("GoPro", link.getBrand());
    assertEquals("Amazon US", link.getSeller());
    assertEquals("+ FREE Delivery", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    Link link = site.test(Helpers.getHtmlPath(site, 4));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("B07G9ZGL9X", link.getSku());
    assertEquals("M&M's Milk Chocolate Party Size Bucket (640g) (Packaging may vary)", link.getName());
    assertEquals("9.28", link.getPrice().toString());
    assertEquals("M&M'S", link.getBrand());
    assertEquals("Amazon AU", link.getSeller());
    assertEquals("& Free Delivery on eligible orders See details and conditions", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

}
