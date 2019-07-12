package io.inprice.scrapper.worker.websites.ca;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class Walmart_CA_Test {

    private final String SITE_NAME = "walmart";
    private final String COUNTRY_CODE = "ca";

    private final Walmart site = spy(new Walmart(new Link()));
    private final HttpResponse response = mock(HttpResponse.class);

    public Walmart_CA_Test() {
        PowerMockito.mockStatic(HttpClient.class);
        when(site.getUrl()).thenReturn("");
    }

    @Test
    public void test_product_1() {
        setMock(1);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("6000199211475", link.getSku());
        assertEquals("hometrends Tuscany Cuddle Chair", link.getName());
        assertEquals("224.00", link.getPrice().toString());
        assertEquals("hometrends", link.getBrand());
        assertEquals("Walmart", link.getSeller());
        assertEquals("Sold & shipped by Walmart", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("6000187311310", link.getSku());
        assertEquals("Armor All Complete Car Care Kit", link.getName());
        assertEquals("21.97", link.getPrice().toString());
        assertEquals("Armor All", link.getBrand());
        assertEquals("Walmart", link.getSeller());
        assertEquals("Sold & shipped by Walmart", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("6000188988578", link.getSku());
        assertEquals("Trudeau Maison Misto Party Grill", link.getName());
        assertEquals("35.00", link.getPrice().toString());
        assertEquals("Trudeau Maison", link.getBrand());
        assertEquals("Walmart", link.getSeller());
        assertEquals("Sold & shipped by Walmart", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.NOT_AVAILABLE, link.getStatus());
        assertEquals("6000196486964", link.getSku());
        assertEquals("Scunci No-Slip Silicone Elastics", link.getName());
        assertEquals("0.00", link.getPrice().toString());
        assertEquals("Scunci", link.getBrand());
        assertEquals("NA", link.getSeller());
        assertEquals("Sold & shipped by NA", link.getShipment());
        assertNull(link.getSpecList());
    }

    private void setMock(int no) {
        String data = null;
        try {
            InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no)).openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(data);
        when(HttpClient.post(anyString(), anyString())).thenReturn(response);
    }

}
