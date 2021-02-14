package io.inprice.parser.websites.uk;

import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Apple_UK_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Website site = new AppleUK();

  @Test
  public void test_product_1() {
    final String sku = "MV752ZM/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("40mm Dragon Fruit Sport Band - S/M & M/L", link.getName());
    assertEquals("49.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_2() {
    final String sku = "MRY62B/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("iPhone XR 64GB (PRODUCT)RED", link.getName());
    assertEquals("749.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_3() {
    final String sku = "MU6K2B/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals(
        "Apple Watch Nike+ Series 4 GPS, 44mm Silver Aluminium Case with Pure Platinum/Black Nike Sport Band",
        link.getName());
    assertEquals("429.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_4() {
    final String sku = "HLL52ZM/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Belkin Ultra High Speed 4K HDMI Cable (2m)", link.getName());
    assertEquals("29.95", link.getPrice().toString());
    assertEquals("Belkin", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
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
          "            'displayName': 'Standard Shipping'," +
          "            'date': 'Within 2-5 business days after shipping'," +
          "            'shippingCost': 'Free'" +
          "          }" +
          "        ]," +
          "        'promoMessage': 'Free Shipping'," +
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
