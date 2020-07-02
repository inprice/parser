package io.inprice.parser.websites.uk;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Asos_UK_Test {

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
    public void test_product_1() {
        setMock(1);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("12382834", competitor.getSku());
        assertEquals("Lacoste Ampthill in black leather", competitor.getName());
        assertEquals("103.69", competitor.getPrice().toString());
        assertEquals("Lacoste", competitor.getBrand());
        assertEquals("ASOS", competitor.getSeller());
        assertEquals("See delivery and returns info", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9021109", competitor.getSku());
        assertEquals("Nike clear water bottle", competitor.getName());
        assertEquals("7.60", competitor.getPrice().toString());
        assertEquals("Nike", competitor.getBrand());
        assertEquals("ASOS", competitor.getSeller());
        assertEquals("See delivery and returns info", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("11903288", competitor.getSku());
        assertEquals("Sass & Belle cutie cat toothbrush holder", competitor.getName());
        assertEquals("8.99", competitor.getPrice().toString());
        assertEquals("Sass & Belle", competitor.getBrand());
        assertEquals("ASOS", competitor.getSeller());
        assertEquals("See delivery and returns info", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("11837256", competitor.getSku());
        assertEquals("Liquorish mix and match floral and polka print wrap skirt", competitor.getName());
        assertEquals("35.95", competitor.getPrice().toString());
        assertEquals("Liquorish", competitor.getBrand());
        assertEquals("ASOS", competitor.getSeller());
        assertEquals("See delivery and returns info", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    private void setMock(int no) {
        String data = null;
        try {
            InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no)).openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(data);
        when(httpClient.get(anyString())).thenReturn(mockResponse);
    }

}
