package io.inprice.parser.websites.exceptions;

import kong.unirest.HttpResponse;
import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.uk.Asos;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Asos_Test {

    private final String SITE_NAME = "asos";
    private final String COUNTRY_CODE = "uk";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Asos site =
        Mockito.spy(
            new Asos(
                new Link()
            )
        );

    @Test
    public void test_for_no_data() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.get(anyString())).thenReturn(mockResponse);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(LinkStatus.NO_DATA, link.getStatus());
    }

}
