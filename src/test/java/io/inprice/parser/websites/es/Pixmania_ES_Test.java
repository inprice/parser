package io.inprice.parser.websites.es;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import kong.unirest.HttpResponse;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Pixmania_ES_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Pixmania site = Mockito.spy(new Pixmania());

  @Test
  public void test_product_1() {
    setMock(1);
    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("LIBH60WH", link.getSku());
    assertEquals(
        "Casco para Bici Inteligente Blanco (Luz,Intermitentes,Manos libres,Reproductor,Aviso caída) y Mando Multifunción",
        link.getName());
    assertEquals("99.99", link.getPrice().toString());
    assertEquals("Livall", link.getBrand());
    assertEquals("Ascendeo Iberia ES", link.getSeller());
    assertEquals("Free shipping: true", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    setMock(2);
    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("502773", link.getSku());
    assertEquals("Alpine 64 PLUS Procesador Enfriador", link.getName());
    assertEquals("30.95", link.getPrice().toString());
    assertEquals("ARCTIC", link.getBrand());
    assertEquals("PIXMANIA ESPANA", link.getSeller());
    assertEquals("Free shipping: true", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    setMock(3);
    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("VLVB34700B30", link.getSku());
    assertEquals("Cable HDMI de alta velocidad con conector HDMIEthe", link.getName());
    assertEquals("17.86", link.getPrice().toString());
    assertEquals("VALUELINE", link.getBrand());
    assertEquals("IberiaPC ES", link.getSeller());
    assertEquals("Free shipping: true", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    setMock(4);
    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("1022729", link.getSku());
    assertEquals("6 discos lavables", link.getName());
    assertEquals("28.95", link.getPrice().toString());
    assertEquals("PHILIPS AVENT", link.getBrand());
    assertEquals("PIXMANIA ESPANA", link.getSeller());
    assertEquals("Free shipping: true", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  private void setMock(int no) {
    when(site.getUrl()).thenReturn("");
    when(site.getPayload()).thenReturn(new HashMap<>());
    when(site.willHtmlBePulled()).thenReturn(false);

    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(getFileContent(no));
    when(httpClient.get(anyString(), anyMap())).thenReturn(mockResponse);
  }

  private String getFileContent(int no) {
    return Helpers.readFile(String.format("websites/%s/%s_%d.json", site.getCountry().getCode(), site.getSiteName(), no));
  }

}
