package io.inprice.scrapper.worker.websites.uk;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class Asda_UK_Test {

    private final String SITE_NAME = "asda";
    private final String COUNTRY_CODE = "uk";

    private final Asda site = spy(new Asda(new Link()));
    private final HttpResponse response = mock(HttpResponse.class);

    public Asda_UK_Test() {
        PowerMockito.mockStatic(HttpClient.class);
    }

    @Test
    public void test_product_1() {
        final String prodId = "1000034704516";

        setMock(1, prodId);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(prodId, link.getSku());
        assertEquals("Gran Lomo Malbec", link.getName());
        assertEquals("5.00", link.getPrice().toString());
        assertEquals("Gran Lomo", link.getBrand());
        assertEquals("Asda", link.getSeller());
        assertEquals("In-store pickup.", link.getShipment());
    }

    @Test
    public void test_product_2() {
        final String prodId = "910000317601";

        setMock(2, prodId);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(prodId, link.getSku());
        assertEquals("Purina ONE Adult Dry Cat Food Salmon and Wholegrain", link.getName());
        assertEquals("11.00", link.getPrice().toString());
        assertEquals("Purina ONE", link.getBrand());
        assertEquals("Asda", link.getSeller());
        assertEquals("In-store pickup.", link.getShipment());
    }

    @Test
    public void test_product_3() {
        final String prodId = "1000122184231";

        setMock(3, prodId);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(prodId, link.getSku());
        assertEquals("Prince Chocolate Sandwich Biscuits", link.getName());
        assertEquals("1.00", link.getPrice().toString());
        assertEquals("Prince", link.getBrand());
        assertEquals("Asda", link.getSeller());
        assertEquals("In-store pickup.", link.getShipment());
    }

    @Test
    public void test_product_4() {
        final String prodId = "910001116620";

        setMock(4, prodId);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals(prodId, link.getSku());
        assertEquals("Oral-B Pro 600 White & Clean Rechargeable Electric Toothbrush", link.getName());
        assertEquals("15.00", link.getPrice().toString());
        assertEquals("Oral-B", link.getBrand());
        assertEquals("Asda", link.getSeller());
        assertEquals("In-store pickup.", link.getShipment());
    }

    private void setMock(int no, String prodId) {
        String data = null;
        try {
            InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no)).openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        when(site.getUrl()).thenReturn("https://groceries.asda.com/" + prodId);
        when(site.willHtmlBePulled()).thenReturn(false);

        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(data);
        when(HttpClient.get(anyString())).thenReturn(response);
    }

}
