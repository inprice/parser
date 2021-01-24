package io.inprice.parser.websites.uk;

import static org.junit.Assert.assertEquals;
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
public class Asda_UK_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Asda site = Mockito.spy(new Asda());

  @Test
  public void test_product_1() {
    final String prodId = "1000034704516";

    setMock(1, prodId);
    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(prodId, link.getSku());
    assertEquals("Gran Lomo Malbec", link.getName());
    assertEquals("5.00", link.getPrice().toString());
    assertEquals("Gran Lomo", link.getBrand());
    assertEquals("Asda", link.getSeller());
    assertEquals("In-store pickup.", link.getShipment());
  }

  @Test
  public void test_product_2() {
    final String prodId = "910000317601";

    setMock(2, prodId);
    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(prodId, link.getSku());
    assertEquals("Purina ONE Adult Dry Cat Food Salmon and Wholegrain", link.getName());
    assertEquals("11.00", link.getPrice().toString());
    assertEquals("Purina ONE", link.getBrand());
    assertEquals("Asda", link.getSeller());
    assertEquals("In-store pickup.", link.getShipment());
  }

  @Test
  public void test_product_3() {
    final String prodId = "1000122184231";

    setMock(3, prodId);
    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(prodId, link.getSku());
    assertEquals("Prince Chocolate Sandwich Biscuits", link.getName());
    assertEquals("1.00", link.getPrice().toString());
    assertEquals("Prince", link.getBrand());
    assertEquals("Asda", link.getSeller());
    assertEquals("In-store pickup.", link.getShipment());
  }

  @Test
  public void test_product_4() {
    final String prodId = "910001116620";

    setMock(4, prodId);
    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(prodId, link.getSku());
    assertEquals("Oral-B Pro 600 White & Clean Rechargeable Electric Toothbrush", link.getName());
    assertEquals("15.00", link.getPrice().toString());
    assertEquals("Oral-B", link.getBrand());
    assertEquals("Asda", link.getSeller());
    assertEquals("In-store pickup.", link.getShipment());
  }

  private void setMock(int no, String prodId) {
    when(site.getUrl()).thenReturn("https://groceries.asda.com/" + prodId);
    when(site.willHtmlBePulled()).thenReturn(false);

    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(getFileContent(no));
    when(httpClient.get(anyString())).thenReturn(mockResponse);
  }

  private String getFileContent(int no) {
    return Helpers.readFile(String.format("websites/%s/%s_%d.json", site.getCountry().getCode(), site.getSiteName(), no));
  }

}
