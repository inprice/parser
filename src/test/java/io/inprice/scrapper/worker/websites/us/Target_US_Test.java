package io.inprice.scrapper.worker.websites.us;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.ca.Walmart;
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
public class Target_US_Test {

    private final String SITE_NAME = "target";
    private final String COUNTRY_CODE = "us";

    private final Target site = spy(new Target(new Link()));
    private final HttpResponse response = mock(HttpResponse.class);

    public Target_US_Test() {
        PowerMockito.mockStatic(HttpClient.class);
    }

    @Test
    public void test_product_1() {
        setMock(1);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

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
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.OUT_OF_STOCK, link.getStatus());
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
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

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
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

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

        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(data);
        when(HttpClient.get(anyString())).thenReturn(response);
    }

}