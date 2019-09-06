package io.inprice.scrapper.worker.websites.us;

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

public class Target_US_Test {

    private final String SITE_NAME = "target";
    private final String COUNTRY_CODE = "us";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Target site =
        Mockito.spy(
            new Target(
                new Link()
            )
        );

    @Test
    public void test_product_1() {
        setMock(1);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("76150374", link.getSku());
        assertEquals("NERF Fortnite SP-L Elite Dart Blaster with 6 Official Nerf Fortnite Elite Darts", link.getName());
        assertEquals("16.69", link.getPrice().toString());
        assertEquals("Nerf", link.getBrand());
        assertEquals("HASBRO INTN'L TRDG BV", link.getSeller());
        assertEquals("Free order pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(Status.NOT_AVAILABLE, link.getStatus());
        assertEquals("52459899", link.getSku());
        assertEquals("Alena Wood Free Standing Cheval Mirror Jewelry Armoire - Baxton Studio", link.getName());
        assertEquals("161.09", link.getPrice().toString());
        assertEquals("Baxton Studio", link.getBrand());
        assertEquals("Target", link.getSeller());
        assertEquals("Free order pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("50608360", link.getSku());
        assertEquals("Instant Pot Duo 6qt 7-in-1 Pressure Cooker", link.getName());
        assertEquals("79.95", link.getPrice().toString());
        assertEquals("Instant Pot", link.getBrand());
        assertEquals("DOUBLE INSIGHT INC", link.getSeller());
        assertEquals("Free order pickup", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("52361921", link.getSku());
        assertEquals("TURTLE BEACH&#174; Recon Chat Wired Gaming Headset for Xbox One", link.getName());
        assertEquals("19.95", link.getPrice().toString());
        assertEquals("Microsoft", link.getBrand());
        assertEquals("VOYETRA TURTLE BEACH INC", link.getSeller());
        assertEquals("Free order pickup", link.getShipment());
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
