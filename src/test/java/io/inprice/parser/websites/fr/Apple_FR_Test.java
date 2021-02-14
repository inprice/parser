package io.inprice.parser.websites.fr;

import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Apple_FR_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Website site = new AppleFR();

  @Test
  public void test_product_1() {
    final String sku = "MV8T2ZM/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Casque Beats Solo3 sans fil - Collection Club de Beats - Rouge Club", link.getName());
    assertEquals("199.95", link.getPrice().toString());
    assertEquals("Beats by Dr. Dre", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Livraison: Gratuite", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    final String sku = "MTEH2ZM/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Housse en cuir pour MacBook Air et MacBook Pro 13 pouces - Noir", link.getName());
    assertEquals("199.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Livraison: Gratuite", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    final String sku = "MPTL2F/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Smart Keyboard pour iPad Air 10,5 pouces (français)", link.getName());
    assertEquals("179.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Livraison: Gratuite", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_4() {
    final String sku = "MVQQ2ZM/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Smart Battery Case pour iPhone XS Max – Rose des sables", link.getName());
    assertEquals("149.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Livraison: Gratuite", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  private void setMocks(String sku) {
    final String SHIPMENT_DATA =
      String.format(
        "{" +
          "'head': {" +
          "  'status': '200'," +
          "  'data': {}" +
          "}," +
          "'body': {" +
          "  'content': {" +
          "    'deliveryMessage': {" +
          "      'geoLocated': false," +
          "      'dudeCookieSet': false," +
          "      '%s': {" +
          "        'orderByDeliveryBy': 'Delivery:'," +
          "        'deliveryOptionMessages': [" +
          "          'In stock'" +
          "        ]," +
          "        'deliveryOptions': [" +
          "          {" +
          "            'displayName': 'Livraison'," +
          "            'date': 'Mer. 19 Juin', " +
          "            'shippingCost': 'Gratuite', " +
          "          }" +
          "        ]," +
          "        'deliveryOptionsLink': {" +
          "          'text': 'Get delivery dates'," +
          "          'dataVar': {}," +
          "          'newTab': false" +
          "        }," +
          "        'showDeliveryOptionsLink': true," +
          "        'messageType': 'Ship'," +
          "        'basePartNumber': 'MTXP2'," +
          "        'commitCodeId': '0'," +
          "        'defaultLocationEnabled': false," +
          "        'idl': false," +
          "        'isBuyable': true" +
          "      }," +
          "      'locationCookieValueFoundForThisCountry': false," +
          "      'dudeLocated': false," +
          "      'accessibilityDeliveryOptions': 'delivery options'," +
          "      'little': false" +
          "    }" +
          "  }" +
          "}" +
        "}",
        sku
      );

    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(SHIPMENT_DATA);
    when(httpClient.get(anyString(), anyString())).thenReturn(mockResponse);
  }

}
