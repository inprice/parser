package io.inprice.parser.websites.uk;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Asos_UK_Test {

  private final String SITE_NAME = "asos";
  private final String COUNTRY_CODE = "uk";

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Asos site = Mockito.spy(new Asos());

  @Test
  public void test_product_1() {
    setMock(1);
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("12382834", link.getSku());
    assertEquals("Lacoste Ampthill in black leather", link.getName());
    assertEquals("103.69", link.getPrice().toString());
    assertEquals("Lacoste", link.getBrand());
    assertEquals("ASOS", link.getSeller());
    assertEquals("See delivery and returns info", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    setMock(2);
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("9021109", link.getSku());
    assertEquals("Nike clear water bottle", link.getName());
    assertEquals("7.60", link.getPrice().toString());
    assertEquals("Nike", link.getBrand());
    assertEquals("ASOS", link.getSeller());
    assertEquals("See delivery and returns info", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    setMock(3);
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("11903288", link.getSku());
    assertEquals("Sass & Belle cutie cat toothbrush holder", link.getName());
    assertEquals("8.99", link.getPrice().toString());
    assertEquals("Sass & Belle", link.getBrand());
    assertEquals("ASOS", link.getSeller());
    assertEquals("See delivery and returns info", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    setMock(4);
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("11837256", link.getSku());
    assertEquals("Liquorish mix and match floral and polka print wrap skirt", link.getName());
    assertEquals("35.95", link.getPrice().toString());
    assertEquals("Liquorish", link.getBrand());
    assertEquals("ASOS", link.getSeller());
    assertEquals("See delivery and returns info", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  private void setMock(int no) {
    String data = null;
    try {
      InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no))
          .openStream();
      data = CharStreams.toString(new InputStreamReader(is));
    } catch (IOException e) {
      e.printStackTrace();
    }

    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(data);
    when(httpClient.get(anyString())).thenReturn(mockResponse);
  }

}
