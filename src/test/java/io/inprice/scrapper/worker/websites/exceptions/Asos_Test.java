package io.inprice.scrapper.worker.websites.exceptions;

import kong.unirest.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.uk.Asos;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
                new Competitor()
            )
        );

    @Test
    public void test_for_no_data() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.get(anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.NO_DATA, competitor.getStatus());
    }

}
