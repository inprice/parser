package io.inprice.parser.websites.au;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import kong.unirest.HttpResponse;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Apple_AU_Test {

  private final String SITE_NAME = "apple";
  private final String COUNTRY_CODE = "au";

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Website site = new io.inprice.parser.websites.xx.Apple(COUNTRY_CODE);

  @Test
  public void test_product_1() {
    final String sku = "MTXP2X/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("11-inch iPad Pro Wi-Fi 64GB — Silver", link.getName());
    assertEquals("1229.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_2() {
    final String sku = "MRE92X/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("13-inch MacBook Air — Space Grey", link.getName());
    assertEquals("2149.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_3() {
    final String sku = "MQ7F2X/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("iPhone 8 256GB Space Grey", link.getName());
    assertEquals("1229.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_4() {
    final String sku = "MQHV2X/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("HomePod — White", link.getName());
    assertEquals("469.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  private void setMocks(String sku) {
    final String SHIPMENT_DATA =
      String.format(
        "{" +
            "  'head': {" +
            "    'status': '200'," +
            "    'data': {}" +
            "  }," +
            "  'body': {" +
            "    'content': {" +
            "      'deliveryMessage': {" +
            "        'geoLocated': false," +
            "        'dudeCookieSet': false," +
            "        '%s': {" +
            "          'orderByDeliveryBy': 'Delivery:'," +
            "          'deliveryOptionMessages': [" +
            "            'In stock'" +
            "          ]," +
            "          'deliveryOptions': [" +
            "            {" +
            "              'displayName': 'Standard Shipping'," +
            "              'date': 'Within 2-5 business days after shipping'," +
            "              'shippingCost': 'Free'" +
            "            }" +
            "          ]," +
            "          'promoMessage': 'Free shipping'," +
            "          'deliveryOptionsLink': {" +
            "            'text': 'Get delivery dates'," +
            "            'dataVar': {}," +
            "            'newTab': false" +
            "          }," +
            "          'showDeliveryOptionsLink': true," +
            "          'messageType': 'Ship'," +
            "          'basePartNumber': 'MTXP2'," +
            "          'commitCodeId': '0'," +
            "          'defaultLocationEnabled': false," +
            "          'idl': false," +
            "          'isBuyable': true" +
            "        }," +
            "        'locationCookieValueFoundForThisCountry': false," +
            "        'dudeLocated': false," +
            "        'accessibilityDeliveryOptions': 'delivery options'," +
            "        'little': false" +
            "      }" +
            "    }" +
            "  }" +
        "}",
        sku
      );

    when(mockResponse.getStatus()).thenReturn(200);
    when(mockResponse.getBody()).thenReturn(SHIPMENT_DATA);
    when(httpClient.get(anyString(), anyString())).thenReturn(mockResponse);
  }

}
