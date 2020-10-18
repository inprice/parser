package io.inprice.parser.websites.tr;

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
public class Apple_TR_Test {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "tr";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Website site =
        new io.inprice.parser.websites.xx.Apple(
            new Link(
                String.format("https://www.apple.com/%s/shop/", COUNTRY_CODE)
            )
        );

    @Test
    public void test_product_1() {
        final String sku = "HJ162ZM/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("SteelSeries Nimbus Kablosuz Oyun Kumandası", link.getName());
        assertEquals("549.00", link.getPrice().toString());
        assertEquals("SteelSeries", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        final String sku = "MPXT2TU/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("13 inç MacBook Pro - Uzay Grisi", link.getName());
        assertEquals("11399.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        final String sku = "MQ6H2TU/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("iPhone 8 64 GB Gümüş", link.getName());
        assertEquals("5799.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        final String sku = "MU662TU/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("Apple Watch Series 4 GPS, 40 mm Uzay Grisi Alüminyum Kasa ve Siyah Spor Kordon", link.getName());
        assertEquals("3099.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Ücretsiz Gönderim", link.getShipment());
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
                        "        'promoMessage': 'Ücretsiz Gönderim'," +
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
