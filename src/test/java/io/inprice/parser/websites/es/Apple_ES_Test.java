package io.inprice.parser.websites.es;

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
public class Apple_ES_Test {

	private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
	private HttpClient httpClient = Mockito.mock(HttpClient.class);

	private final Website site = new AppleES();

	@Test
	public void test_product_1() {
		final String sku = "MV7N2TY/A";
		setMocks(sku);

		Link link = site.test(Helpers.getHtmlPath(site, 1), httpClient);

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals(sku, link.getSku());
		assertEquals("AirPods con estuche de carga", link.getName());
		assertEquals("179.00", link.getPrice().toString());
		assertEquals("Apple", link.getBrand());
		assertEquals("Apple", link.getSeller());
		assertEquals("Envío gratuito", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		final String sku = "MRTR2Y/A";
		setMocks(sku);

		Link link = site.test(Helpers.getHtmlPath(site, 2), httpClient);

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals(sku, link.getSku());
		assertEquals("Mac mini", link.getName());
		assertEquals("899.00", link.getPrice().toString());
		assertEquals("Apple", link.getBrand());
		assertEquals("Apple", link.getSeller());
		assertEquals("Envío gratuito", link.getShipment());
		assertNull(link.getSpecList());
	}

	@Test
	public void test_product_3() {
		final String sku = "MK0C2ZM/A";
		setMocks(sku);

		Link link = site.test(Helpers.getHtmlPath(site, 3), httpClient);

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals(sku, link.getSku());
		assertEquals("Apple Pencil", link.getName());
		assertEquals("99.00", link.getPrice().toString());
		assertEquals("Apple", link.getBrand());
		assertEquals("Apple", link.getSeller());
		assertEquals("Envío gratuito", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_4() {
		final String sku = "MN932QL/A";
		setMocks(sku);

		Link link = site.test(Helpers.getHtmlPath(site, 4), httpClient);

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals(sku, link.getSku());
		assertEquals("iPhone 7 de 128 GB en plata", link.getName());
		assertEquals("639.00", link.getPrice().toString());
		assertEquals("Apple", link.getBrand());
		assertEquals("Apple", link.getSeller());
		assertEquals("Envío gratuito", link.getShipment());
		assertNull(link.getSpecList());
	}

	private void setMocks(String sku) {
		final String SHIPMENT_DATA = String.format("{" + "'head': {" + "  'status': '200'," + "  'data': {}" + "},"
		    + "'body': {" + "  'content': {" + "    'deliveryMessage': {" + "      'geoLocated': false,"
		    + "      'dudeCookieSet': false," + "      '%s': {" + "        'orderByDeliveryBy': 'Delivery:',"
		    + "        'deliveryOptionMessages': [" + "          'In stock'" + "        ]," + "        'deliveryOptions': ["
		    + "          {" + "            'displayName': 'Standard Shipping',"
		    + "            'date': 'Within 2-5 business days after shipping'," + "            'shippingCost': 'Free'"
		    + "          }" + "        ]," + "        'promoMessage': 'Envío gratuito',"
		    + "        'deliveryOptionsLink': {" + "          'text': 'Get delivery dates'," + "          'dataVar': {},"
		    + "          'newTab': false" + "        }," + "        'showDeliveryOptionsLink': true,"
		    + "        'messageType': 'Ship'," + "        'basePartNumber': 'MTXP2'," + "        'commitCodeId': '0',"
		    + "        'defaultLocationEnabled': false," + "        'idl': false," + "        'isBuyable': true"
		    + "      }," + "      'locationCookieValueFoundForThisCountry': false," + "      'dudeLocated': false,"
		    + "      'accessibilityDeliveryOptions': 'delivery options'," + "      'little': false" + "    }" + "  }" + "}"
		    + "}", sku);

		when(mockResponse.getStatus()).thenReturn(200);
		when(mockResponse.getBody()).thenReturn(SHIPMENT_DATA);
		when(httpClient.get(anyString(), anyString())).thenReturn(mockResponse);
	}

}
