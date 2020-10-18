package io.inprice.parser.websites.exceptions;

import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.ca.Walmart;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Walmart_CA_Test {

    private final String SITE_NAME = "walmart";
    private final String COUNTRY_CODE = "ca";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Walmart site =
        Mockito.spy(
            new Walmart(
                new Link()
            )
        );

    @Test
    public void test_for_no_data() {
        when(site.getPayload()).thenReturn("Fake Payload!");

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn("{}");
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(LinkStatus.NO_DATA, link.getStatus());
    }

    @Test
    public void test_for_read_error() {
        when(site.getPayload()).thenReturn(null);

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn("{}");
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getEmptyHtmlPath(), httpClient);

        assertEquals(LinkStatus.READ_ERROR, link.getStatus());
    }

    @Test
    public void test_for_socket_error() {
        when(site.getPayload()).thenReturn("Fake Payload!");

        when(mockResponse.getStatus()).thenReturn(0);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertTrue(link.getHttpStatus() == 0);
        assertEquals(LinkStatus.SOCKET_ERROR, link.getStatus());
    }

    @Test
    public void test_for_network_error() {
        when(site.getPayload()).thenReturn("Fake Payload!");

        when(mockResponse.getStatus()).thenReturn(400);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertTrue(link.getHttpStatus() == 400);
        assertEquals(LinkStatus.NETWORK_ERROR, link.getStatus());
    }

}
