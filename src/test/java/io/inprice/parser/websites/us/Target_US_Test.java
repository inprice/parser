package io.inprice.parser.websites.us;

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
public class Target_US_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Target site = Mockito.spy(new Target());

  @Test
  public void test_product_1() {
    setMock(1);
    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("76150374", link.getSku());
    assertEquals("NERF Fortnite SP-L Elite Dart Blaster with 6 Official Nerf Fortnite Elite Darts", link.getName());
    assertEquals("16.69", link.getPrice().toString());
    assertEquals("Nerf", link.getBrand());
    assertEquals("HASBRO INTN\\'L TRDG BV", link.getSeller());
    assertEquals("Free order pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    setMock(2);
    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
    assertEquals("52459899", link.getSku());
    assertEquals("Alena Wood Free Standing Cheval Mirror Jewelry Armoire - Baxton Studio", link.getName());
    assertEquals("161.09", link.getPrice().toString());
    assertEquals("Baxton Studio", link.getBrand());
    assertEquals("Target", link.getSeller());
    assertEquals("Free order pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    setMock(3);
    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("50608360", link.getSku());
    assertEquals("Instant Pot Duo 6qt 7-in-1 Pressure Cooker", link.getName());
    assertEquals("79.95", link.getPrice().toString());
    assertEquals("Instant Pot", link.getBrand());
    assertEquals("DOUBLE INSIGHT INC", link.getSeller());
    assertEquals("Free order pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    setMock(4);
    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("52361921", link.getSku());
    assertEquals("TURTLE BEACH&#174; Recon Chat Wired Gaming Headset for Xbox One", link.getName());
    assertEquals("19.95", link.getPrice().toString());
    assertEquals("Microsoft", link.getBrand());
    assertEquals("VOYETRA TURTLE BEACH INC", link.getSeller());
    assertEquals("Free order pickup", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  private void setMock(int no) {
    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(getFileContent(no));
    when(httpClient.get(anyString())).thenReturn(mockResponse);
  }

  private String getFileContent(int no) {
    return Helpers.readFile(String.format("websites/%s/%s_%d.json", site.getCountry().getCode(), site.getSiteName(), no));
  }

}
