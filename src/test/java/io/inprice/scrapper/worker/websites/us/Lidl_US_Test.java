package io.inprice.scrapper.worker.websites.us;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.helpers.HttpClient;
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
public class Lidl_US_Test {

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    @Test
    public void test_product_1() {
        setMocks(1);

        Competitor competitor = new Lidl(new Competitor("https://www.lidl.com/products/285939_A")).test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("285939_A", competitor.getSku());
        assertEquals("train set", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMocks(2);

        Competitor competitor = new Lidl(new Competitor("https://www.lidl.com/products/311073_A")).test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("311073_A", competitor.getSku());
        assertEquals("inflatable tandem kayak", competitor.getName());
        assertEquals("49.99", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMocks(3);

        Competitor competitor = new Lidl(new Competitor("https://www.lidl.com/products/310436_C")).test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("310436_C", competitor.getSku());
        assertEquals("pastel color paint, graphite", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMocks(4);

        Competitor competitor = new Lidl(new Competitor("https://www.lidl.com/products/1031629")).test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1031629", competitor.getSku());
        assertEquals("classic ciabatta", competitor.getName());
        assertEquals("1.79", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("Lidl", competitor.getSeller());
        assertEquals("In-store pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    private void setMocks(int no) {
        String data = null;
        try {
            InputStream is = Resources.getResource("websites/us/lidl_" + no + ".json").openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(data);
        when(httpClient.get(anyString())).thenReturn(mockResponse);
    }

}
