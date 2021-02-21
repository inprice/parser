package io.inprice.parser.websites.ca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import kong.unirest.HttpResponse;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CanadianTire_CA_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final CanadianTire site = new CanadianTire();

  @Test
  public void test_product_1() {
    setMock(1);
    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("0791265", link.getSku());
    assertEquals("Pelican Kayak Paddle, Green, 84-in", link.getName());
    assertEquals("49.99", link.getPrice().toString());
    assertEquals("Pelican", link.getBrand());
    assertEquals("CanadianTire", link.getSeller());
    assertEquals("In-store pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    setMock(2);
    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("0427991", link.getSku());
    assertEquals("Corn Broom", link.getName());
    assertEquals("7.99", link.getPrice().toString());
    assertEquals("Mastercraft", link.getBrand());
    assertEquals("CanadianTire", link.getSeller());
    assertEquals("In-store pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    setMock(3);
    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("0687094", link.getSku());
    assertEquals("Mastercraft Wall-Mounted Bicycle Rack with Shelf", link.getName());
    assertEquals("29.99", link.getPrice().toString());
    assertEquals("Mastercraft", link.getBrand());
    assertEquals("CanadianTire", link.getSeller());
    assertEquals("In-store pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    setMock(4);
    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("0765877", link.getSku());
    assertEquals("Woods Logan Sleeping Bag, -12°C", link.getName());
    assertEquals("119.99", link.getPrice().toString());
    assertEquals("Woods", link.getBrand());
    assertEquals("CanadianTire", link.getSeller());
    assertEquals("In-store pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  private void setMock(int no) {
    when(mockResponse.getStatus()).thenReturn(200);
    //when(mockResponse.getBody()).thenReturn(getFileContent(no));
    when(httpClient.get(anyString())).thenReturn(mockResponse);
  }

  /*
  private String getFileContent(int no) {
    return Helpers.readFile(String.format("websites/%s/%s_%d.json", site.getCountry().getCode(), site.getSiteName(), no));
  }
  */

}
