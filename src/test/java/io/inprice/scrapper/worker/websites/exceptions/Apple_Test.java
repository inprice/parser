package io.inprice.scrapper.worker.websites.exceptions;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Apple_Test {

    private final String SITE_NAME = "apple";
    private final String COUNTRY_CODE = "au";

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
    public void test_for_no_data() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.get(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(Status.NO_DATA, link.getStatus());
    }

    @Test
    public void test_for_read_error() {
        Link link = site.test(Helpers.getEmptyHtmlPath());

        assertEquals(Status.READ_ERROR, link.getStatus());
    }

    @Test
    public void test_for_socket_error() {
        when(mockResponse.getStatus()).thenReturn(0);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.get(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertTrue(link.getHttpStatus() == 0);
        assertEquals(Status.SOCKET_ERROR, link.getStatus());
    }

    @Test
    public void test_for_network_error() {
        when(mockResponse.getStatus()).thenReturn(400);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.get(anyString(), anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertTrue(link.getHttpStatus() == 400);
        assertEquals(Status.NETWORK_ERROR, link.getStatus());
    }

}
