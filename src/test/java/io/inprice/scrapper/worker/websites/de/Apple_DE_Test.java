package io.inprice.scrapper.worker.websites.de;

import kong.unirest.HttpResponse;
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
public class Apple_DE_Test {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "de";

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
        final String sku = "MR912FD/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("Apple TV HD 32 GB", competitor.getName());
        assertEquals("159.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Expressversand. Kostenlos", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        final String sku = "MRT42D/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("21,5\" iMac mit Retina 4K Display", competitor.getName());
        assertEquals("1699.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Expressversand. Kostenlos", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        final String sku = "MTFL2FD/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("12,9\" iPad Pro Wi‑Fi 256 GB – Space Grau", competitor.getName());
        assertEquals("1269.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Expressversand. Kostenlos", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        final String sku = "MRY72ZD/A";
        setMocks(sku);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(sku, competitor.getSku());
        assertEquals("iPhone XR 64 GB Gelb", competitor.getName());
        assertEquals("849.00", competitor.getPrice().toString());
        assertEquals("Apple", competitor.getBrand());
        assertEquals("Apple", competitor.getSeller());
        assertEquals("Expressversand. Kostenlos", competitor.getShipment());
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
                    "        'promoMessage': 'Expressversand. Kostenlos'," +
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
