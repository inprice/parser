package io.inprice.parser.websites.de;

import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Apple_DE_Test {

  private final String SITE_NAME = "apple";
  private final String COUNTRY_CODE = "de";

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Website site = new io.inprice.parser.websites.xx.Apple(COUNTRY_CODE);

  @Test
  public void test_product_1() {
    final String sku = "MR912FD/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Apple TV HD 32 GB", link.getName());
    assertEquals("159.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Expressversand. Kostenlos", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_2() {
    final String sku = "MRT42D/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("21,5\" iMac mit Retina 4K Display", link.getName());
    assertEquals("1699.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Expressversand. Kostenlos", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_3() {
    final String sku = "MTFL2FD/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("12,9\" iPad Pro Wi‑Fi 256 GB – Space Grau", link.getName());
    assertEquals("1269.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Expressversand. Kostenlos", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_4() {
    final String sku = "MRY72ZD/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("iPhone XR 64 GB Gelb", link.getName());
    assertEquals("849.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Expressversand. Kostenlos", link.getShipment());
    assertNull(link.getSpecList());
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
          "            'displayName': 'Standard Shipping'," +
          "            'date': 'Within 2-5 business days after shipping'," +
          "            'shippingCost': 'Free'" +
          "          }" +
          "        ]," +
          "        'promoMessage': 'Expressversand. Kostenlos'," +
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
