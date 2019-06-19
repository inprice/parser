package io.inprice.scrapper.worker.websites.au;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class Apple_AU_Test {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "au";

    private final Website site;

    private final HttpResponse mockResponse = PowerMockito.mock(HttpResponse.class);

    public Apple_AU_Test() {
        Link link = new Link();
        link.setUrl(String.format("https://www.apple.com/%s/shop/", COUNTRY_CODE));
        site = new io.inprice.scrapper.worker.websites.xx.Apple(link);

        PowerMockito.mockStatic(HttpClient.class);
    }

    @Test
    public void test_product_1() {
        final String sku = "MTXP2X/A";
        setMocks(sku);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
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

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
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

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
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

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
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
        when(HttpClient.get(anyString(), anyString())).thenReturn(mockResponse);
    }

}
