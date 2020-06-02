package io.inprice.scrapper.worker.websites.au;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Apple_AU_Test {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "au";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Website site =
        new io.inprice.scrapper.worker.websites.xx.Apple(
            new Competitor(
                String.format("https://www.apple.com/%s/shop/", COUNTRY_CODE)
            )
        );

    @Test
    public void test_product_1() {
        final String sku = "MTXP2X/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("11-inch iPad Pro Wi-Fi 64GB — Silver", competitor.getName());
        assertEquals("1229.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        final String sku = "MRE92X/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("13-inch MacBook Air — Space Grey", competitor.getName());
        assertEquals("2149.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        final String sku = "MQ7F2X/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("iPhone 8 256GB Space Grey", competitor.getName());
        assertEquals("1229.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        final String sku = "MQHV2X/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("HomePod — White", competitor.getName());
        assertEquals("469.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertNull(competitor.getSpecList());
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
                    "          'deliveryOptionsCompetitor': {" +
                    "            'text': 'Get delivery dates'," +
                    "            'dataVar': {}," +
                    "            'newTab': false" +
                    "          }," +
                    "          'showDeliveryOptionsCompetitor': true," +
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
