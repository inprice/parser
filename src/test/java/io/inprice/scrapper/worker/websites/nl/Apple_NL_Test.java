package io.inprice.scrapper.worker.websites.nl;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Apple_NL_Test {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "nl";

    @Mock
    private HttpResponse mockResponse;

    @Mock
    private HttpClient httpClient;

    private final Website site =
        new io.inprice.scrapper.worker.websites.xx.Apple(
            new Link(
                String.format("https://www.apple.com/%s/shop/", COUNTRY_CODE)
            )
        );

    @Test
    public void test_product_1() {
        final String sku = "MRT42N/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("21,5-inch iMac met Retina 4K‑display", link.getName());
        assertEquals("1699.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free Shipping", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        final String sku = "MT582ZD/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("512‑GB iPhone XS Max, goud", link.getName());
        assertEquals("1659.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free Shipping", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        final String sku = "MRXJ2ZM/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("AirPods met draadloze oplaadcase", link.getName());
        assertEquals("229.00", link.getPrice().toString());
        assertEquals("Apple", link.getBrand());
        assertEquals("Apple", link.getSeller());
        assertEquals("Free Shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        final String sku = "HJ162ZM/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(sku, link.getSku());
        assertEquals("SteelSeries Nimbus draadloze gamecontroller", link.getName());
        assertEquals("59.95", link.getPrice().toString());
        assertEquals("SteelSeries", link.getBrand());
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
