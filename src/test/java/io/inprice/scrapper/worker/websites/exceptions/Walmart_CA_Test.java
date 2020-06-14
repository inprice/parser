package io.inprice.scrapper.worker.websites.exceptions;

import kong.unirest.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.ca.Walmart;
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
                new Competitor()
            )
        );

    @Test
    public void test_for_no_data() {
        when(site.getPayload()).thenReturn("Fake Payload!");

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn("{}");
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.NO_DATA, competitor.getStatus());
    }

    @Test
    public void test_for_read_error() {
        when(site.getPayload()).thenReturn(null);

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn("{}");
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getEmptyHtmlPath(), httpClient);

        assertEquals(CompetitorStatus.READ_ERROR, competitor.getStatus());
    }

    @Test
    public void test_for_socket_error() {
        when(site.getPayload()).thenReturn("Fake Payload!");

        when(mockResponse.getStatus()).thenReturn(0);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertTrue(competitor.getHttpStatus() == 0);
        assertEquals(CompetitorStatus.SOCKET_ERROR, competitor.getStatus());
    }

    @Test
    public void test_for_network_error() {
        when(site.getPayload()).thenReturn("Fake Payload!");

        when(mockResponse.getStatus()).thenReturn(400);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertTrue(competitor.getHttpStatus() == 400);
        assertEquals(CompetitorStatus.NETWORK_ERROR, competitor.getStatus());
    }

}
