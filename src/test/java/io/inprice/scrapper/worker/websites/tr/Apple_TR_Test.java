package io.inprice.scrapper.worker.websites.tr;

import kong.unirest.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
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
        new io.inprice.scrapper.worker.websites.xx.Apple(
            new Competitor(
                String.format("https://www.apple.com/%s/shop/", COUNTRY_CODE)
            )
        );

    @Test
    public void test_product_1() {
        final String sku = "HJ162ZM/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("SteelSeries Nimbus Kablosuz Oyun Kumandası", competitor.getName());
        assertEquals("549.00", competitor.getPrice().toString());
        assertEquals("SteelSeries", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Ücretsiz Gönderim", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        final String sku = "MPXT2TU/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("13 inç MacBook Pro - Uzay Grisi", competitor.getName());
        assertEquals("11399.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Ücretsiz Gönderim", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        final String sku = "MQ6H2TU/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("iPhone 8 64 GB Gümüş", competitor.getName());
        assertEquals("5799.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Ücretsiz Gönderim", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        final String sku = "MU662TU/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("Apple Watch Series 4 GPS, 40 mm Uzay Grisi Alüminyum Kasa ve Siyah Spor Kordon", competitor.getName());
        assertEquals("3099.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Ücretsiz Gönderim", competitor.getShipment());
        assertNull(competitor.getSpecList());
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
                        "        'deliveryOptionsCompetitor': {" +
                        "          'text': 'Get delivery dates'," +
                        "          'dataVar': {}," +
                        "          'newTab': false" +
                        "        }," +
                        "        'showDeliveryOptionsCompetitor': true," +
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
