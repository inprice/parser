package io.inprice.scrapper.worker.websites.exceptions;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.uk.Asos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class Asos_Test {

    private final String SITE_NAME = "asos";
    private final String COUNTRY_CODE = "uk";

    private final Asos site = spy(new Asos(new Link()));
    private final HttpResponse mockResponse = PowerMockito.mock(HttpResponse.class);

    public Asos_Test() {
        PowerMockito.mockStatic(HttpClient.class);
    }

    @Test
    public void test_for_no_data() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(null);
        when(HttpClient.get(anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.NO_DATA, link.getStatus());
    }

    @Test
    public void test_for_read_error() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn("");
        when(HttpClient.get(anyString())).thenReturn(null);

        Link link = site.test(Helpers.getEmptyHtmlPath());

        assertEquals(Status.READ_ERROR, link.getStatus());
    }

    @Test
    public void test_for_socket_error() {
        when(mockResponse.getStatus()).thenReturn(0);
        when(mockResponse.getBody()).thenReturn(null);
        when(HttpClient.get(anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertTrue(link.getHttpStatus() == 0);
        assertEquals(Status.SOCKET_ERROR, link.getStatus());
    }

    @Test
    public void test_for_network_error() {
        when(mockResponse.getStatus()).thenReturn(400);
        when(mockResponse.getBody()).thenReturn(null);
        when(HttpClient.get(anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertTrue(link.getHttpStatus() == 400);
        assertEquals(Status.NETWORK_ERROR, link.getStatus());
    }

}
