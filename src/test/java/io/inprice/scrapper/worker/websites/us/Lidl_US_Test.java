package io.inprice.scrapper.worker.websites.us;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
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

public class Lidl_US_Test {

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    @Test
    public void test_product_1() {
        setMocks(1);

        Link link = new Lidl(new Link("https://www.lidl.com/products/285939_A")).test(null, httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("285939_A", link.getSku());
        assertEquals("train set", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMocks(2);

        Link link = new Lidl(new Link("https://www.lidl.com/products/311073_A")).test(null, httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("311073_A", link.getSku());
        assertEquals("inflatable tandem kayak", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMocks(3);

        Link link = new Lidl(new Link("https://www.lidl.com/products/310436_C")).test(null, httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("310436_C", link.getSku());
        assertEquals("pastel color paint, graphite", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMocks(4);

        Link link = new Lidl(new Link("https://www.lidl.com/products/1031629")).test(null, httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1031629", link.getSku());
        assertEquals("classic ciabatta", link.getName());
        assertEquals("1.79", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("Lidl", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
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
