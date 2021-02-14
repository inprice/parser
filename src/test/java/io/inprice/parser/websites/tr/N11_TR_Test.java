package io.inprice.parser.websites.tr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;

public class N11_TR_Test {

  private final N11 site = new N11();

  @Test
  public void test_product_1() {
    Link link = site.test(Helpers.getHtmlPath(site, 1));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("249823652", link.getSku());
    assertEquals("Seaflo Hidrofor Pompası 4.3 lt. / dak 12 V 35 psi", link.getName());
    assertEquals("123.55", link.getPrice().toString());
    assertEquals("Diğer", link.getBrand());
    assertEquals("eroldenizcilik", link.getSeller());
    assertEquals("Mağazaya özel 150 TL ve üzeri Ücretsiz Kargo", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    Link link = site.test(Helpers.getHtmlPath(site, 2));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("241174632", link.getSku());
    assertEquals("Bel İnceltici Korse Vücut Şekillendirici Doğum Sonrası Toparlayıc", link.getName());
    assertEquals("39.90", link.getPrice().toString());
    assertEquals("Giaxa", link.getBrand());
    assertEquals("ecogroup", link.getSeller());
    assertEquals("Ücretsiz Kargo", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    Link link = site.test(Helpers.getHtmlPath(site, 3));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("31175655", link.getSku());
    assertEquals("Gemici Çelik Erkek Zincir Kolye 7 mm 56 cm h081", link.getName());
    assertEquals("59.90", link.getPrice().toString());
    assertEquals("Takı Dükkanı", link.getBrand());
    assertEquals("takidukkani", link.getSeller());
    assertEquals("Ücretsiz Kargo", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    Link link = site.test(Helpers.getHtmlPath(site, 4));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("223682414", link.getSku());
    assertEquals("Genç Elitler Serisi Kutulu Özel Set (Ciltli 3 Kitap)", link.getName());
    assertEquals("65.84", link.getPrice().toString());
    assertEquals("Marie Lu", link.getBrand());
    assertEquals("pegasusyayinlari", link.getSeller());
    assertEquals("Mağazaya özel 100 TL ve üzeri Ücretsiz Kargo", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_5() {
    Link link = site.test(Helpers.getHtmlPath(site, 5));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("428137685", link.getSku());
    assertEquals("Oppo Realme X2 Pro Duos 128 GB (İthalatçı Garantili)", link.getName());
    assertEquals("4899.00", link.getPrice().toString());
    assertEquals("Oppo", link.getBrand());
    assertEquals("Anatolium", link.getSeller());
    assertEquals("Ücretsiz Kargo", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_6() {
    Link link = site.test(Helpers.getHtmlPath(site, 6));

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("454282185", link.getSku());
    assertEquals("Matte Kar Çorabı - Superx Series", link.getName());
    assertEquals("138.57", link.getPrice().toString());
    assertEquals("Matte", link.getBrand());
    assertEquals("bizymo", link.getSeller());
    assertEquals("NA", link.getShipment());
    assertNull(link.getSpecList());
  }

}
