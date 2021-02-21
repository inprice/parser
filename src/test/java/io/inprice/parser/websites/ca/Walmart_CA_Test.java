package io.inprice.parser.websites.ca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import kong.unirest.HttpResponse;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Walmart_CA_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Walmart site = Mockito.spy(new Walmart());

  @Test
  public void test_product_1() {
    setMock(1);
    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("6000199211475", link.getSku());
    assertEquals("hometrends Tuscany Cuddle Chair", link.getName());
    assertEquals("224.00", link.getPrice().toString());
    assertEquals("hometrends", link.getBrand());
    assertEquals("Walmart", link.getSeller());
    assertEquals("Sold & shipped by Walmart", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    setMock(2);
    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("6000187311310", link.getSku());
    assertEquals("Armor All Complete Car Care Kit", link.getName());
    assertEquals("21.97", link.getPrice().toString());
    assertEquals("Armor All", link.getBrand());
    assertEquals("Walmart", link.getSeller());
    assertEquals("Sold & shipped by Walmart", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    setMock(3);
    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("6000188988578", link.getSku());
    assertEquals("Trudeau Maison Misto Party Grill", link.getName());
    assertEquals("35.00", link.getPrice().toString());
    assertEquals("Trudeau Maison", link.getBrand());
    assertEquals("Walmart", link.getSeller());
    assertEquals("Sold & shipped by Walmart", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    setMock(4);
    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
    assertEquals("6000196486964", link.getSku());
    assertEquals("Scunci No-Slip Silicone Elastics", link.getName());
    assertEquals("0.00", link.getPrice().toString());
    assertEquals("Scunci", link.getBrand());
    assertEquals(Consts.Words.NOT_AVAILABLE, link.getSeller());
    assertEquals("Sold & shipped by NA", link.getShipment());
    assertNull(link.getSpecList());
  }

  private void setMock(int no) {
    when(site.getUrl()).thenReturn("");
    
    when(mockResponse.getStatus()).thenReturn(200);
    //when(mockResponse.getBody()).thenReturn(getFileContent(no));
    when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);
  }

  /*
  private String getFileContent(int no) {
    return Helpers.readFile(String.format("websites/%s/%s_%d.json", site.getCountry().getCode(), site.getSiteName(), no));
  }
  */

}
