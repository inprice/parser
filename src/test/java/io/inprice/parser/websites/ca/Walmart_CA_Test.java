package io.inprice.parser.websites.ca;

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

import static org.junit.Assert.*;
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
    public void test_product_1() {
        setMock(1);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6000199211475", competitor.getSku());
        assertEquals("hometrends Tuscany Cuddle Chair", competitor.getName());
        assertEquals("224.00", competitor.getPrice().toString());
        assertEquals("hometrends", competitor.getBrand());
        assertEquals("Walmart", competitor.getSeller());
        assertEquals("Sold & shipped by Walmart", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6000187311310", competitor.getSku());
        assertEquals("Armor All Complete Car Care Kit", competitor.getName());
        assertEquals("21.97", competitor.getPrice().toString());
        assertEquals("Armor All", competitor.getBrand());
        assertEquals("Walmart", competitor.getSeller());
        assertEquals("Sold & shipped by Walmart", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6000188988578", competitor.getSku());
        assertEquals("Trudeau Maison Misto Party Grill", competitor.getName());
        assertEquals("35.00", competitor.getPrice().toString());
        assertEquals("Trudeau Maison", competitor.getBrand());
        assertEquals("Walmart", competitor.getSeller());
        assertEquals("Sold & shipped by Walmart", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("6000196486964", competitor.getSku());
        assertEquals("Scunci No-Slip Silicone Elastics", competitor.getName());
        assertEquals("0.00", competitor.getPrice().toString());
        assertEquals("Scunci", competitor.getBrand());
        assertEquals("NA", competitor.getSeller());
        assertEquals("Sold & shipped by NA", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    private void setMock(int no) {
        String data = null;
        try {
            InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no)).openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        when(site.getUrl()).thenReturn("");

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(data);
        when(httpClient.post(anyString(), anyString())).thenReturn(mockResponse);
    }

}
