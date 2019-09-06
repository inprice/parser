package io.inprice.scrapper.worker.websites.ca;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class CanadianTire_CA_Test {

    private final String SITE_NAME = "canadiantire";
    private final String COUNTRY_CODE = "ca";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final CanadianTire site =
        new CanadianTire(
            new Link()
        );

    @Test
    public void test_product_1() {
        setMock(1);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("0791265", link.getSku());
        assertEquals("Pelican Kayak Paddle, Green, 84-in", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("Pelican", link.getBrand());
        assertEquals("CanadianTire", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("0427991", link.getSku());
        assertEquals("Corn Broom", link.getName());
        assertEquals("7.99", link.getPrice().toString());
        assertEquals("Mastercraft", link.getBrand());
        assertEquals("CanadianTire", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("0687094", link.getSku());
        assertEquals("Mastercraft Wall-Mounted Bicycle Rack with Shelf", link.getName());
        assertEquals("29.99", link.getPrice().toString());
        assertEquals("Mastercraft", link.getBrand());
        assertEquals("CanadianTire", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("0765877", link.getSku());
        assertEquals("Woods™ Logan Sleeping Bag, -12°C", link.getName());
        assertEquals("119.99", link.getPrice().toString());
        assertEquals("Woods", link.getBrand());
        assertEquals("CanadianTire", link.getSeller());
        assertEquals("In-store pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
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
