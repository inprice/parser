package io.inprice.parser.websites.it;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class VidaXL_IT_Test {

  private final String SITE_NAME = "vidaxl";
  private final String COUNTRY_CODE = "it";

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  @Spy
  private final VidaXL site = Mockito.spy(new VidaXL());

  @Test
  public void test_product_1() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("41192", link.getSku());
    assertEquals("Programmatore timer irrigazione elettronico automatico per orto 1 via", link.getName());
    assertEquals("26.99", link.getPrice().toString());
    assertEquals("vidaXL", link.getBrand());
    assertEquals("vidaXL", link.getSeller());
    assertEquals("Tempo di spedizione : 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL",
        link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("140654", link.getSku());
    assertEquals("vidaXL Scrostatore Pneumatico ad Aghi", link.getName());
    assertEquals("32.99", link.getPrice().toString());
    assertEquals("vidaXL", link.getBrand());
    assertEquals("vidaXL", link.getSeller());
    assertEquals("Tempo di spedizione : 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL",
        link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(getJsonFromFile(3));
    when(httpClient.get(anyString())).thenReturn(mockResponse);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("41296", link.getSku());
    assertEquals("Recinzione con traliccio estensibile di legno 180 x 100 cm", link.getName());
    assertEquals("19.10", link.getPrice().toString());
    assertEquals("VidaXL", link.getBrand());
    assertEquals("VidaXL", link.getSeller());
    assertEquals("Tempo di consegna: 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL",
        link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(getJsonFromFile(4));
    when(httpClient.get(anyString())).thenReturn(mockResponse);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals("141679", link.getSku());
    assertEquals("vidaXL Specchio Traffico Convesso Nero Plastica PC per Esterni 30 cm", link.getName());
    assertEquals("16.00", link.getPrice().toString());
    assertEquals("VidaXL", link.getBrand());
    assertEquals("VidaXL", link.getSeller());
    assertEquals("Tempo di consegna: 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL",
        link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  private String getJsonFromFile(int no) {
    String data = null;
    try {
      InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no))
          .openStream();
      data = CharStreams.toString(new InputStreamReader(is));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return data;
  }

}