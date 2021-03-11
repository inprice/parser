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
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import io.inprice.parser.websites.xx.Apple;
import kong.unirest.HttpResponse;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Apple_CA_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final Website site = new Apple() {};

  @Test
  public void test_product_1() {
    final String sku = "MRT32LL/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("21.5-inch iMac with Retina 4K display", link.getName());
    assertEquals("1699.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_2() {
    final String sku = "MV7N2AM/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("AirPods with Charging Case", link.getName());
    assertEquals("219.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
    assertTrue(link.getSpecList().size() > 0);
  }

  @Test
  public void test_product_3() {
    final String sku = "MQD22CL/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("Apple TV 4K 32GB", link.getName());
    assertEquals("229.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
    assertNull(link.getSpecList());
  }

  @Test
  public void test_product_4() {
    final String sku = "MUUK2VC/A";
    setMocks(sku);

    Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

    assertEquals(LinkStatus.AVAILABLE, link.getStatus());
    assertEquals(sku, link.getSku());
    assertEquals("10.5-inch iPad Air Wi‑Fi 64GB - Silver", link.getName());
    assertEquals("649.00", link.getPrice().toString());
    assertEquals("Apple", link.getBrand());
    assertEquals("Apple", link.getSeller());
    assertEquals("Free Shipping", link.getShipment());
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
