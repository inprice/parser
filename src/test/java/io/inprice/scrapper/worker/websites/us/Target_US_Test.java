package io.inprice.scrapper.worker.websites.us;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
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

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Target_US_Test {

    private final String SITE_NAME = "target";
    private final String COUNTRY_CODE = "us";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Target site =
        Mockito.spy(
            new Target(
                new Competitor()
            )
        );

    @Test
    public void test_product_1() {
        setMock(1);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("76150374", competitor.getSku());
        assertEquals("NERF Fortnite SP-L Elite Dart Blaster with 6 Official Nerf Fortnite Elite Darts", competitor.getName());
        assertEquals("16.69", competitor.getPrice().toString());
        assertEquals("Nerf", competitor.getBrand());
        assertEquals("HASBRO INTN'L TRDG BV", competitor.getSeller());
        assertEquals("Free order pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("52459899", competitor.getSku());
        assertEquals("Alena Wood Free Standing Cheval Mirror Jewelry Armoire - Baxton Studio", competitor.getName());
        assertEquals("161.09", competitor.getPrice().toString());
        assertEquals("Baxton Studio", competitor.getBrand());
        assertEquals("Target", competitor.getSeller());
        assertEquals("Free order pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("50608360", competitor.getSku());
        assertEquals("Instant Pot Duo 6qt 7-in-1 Pressure Cooker", competitor.getName());
        assertEquals("79.95", competitor.getPrice().toString());
        assertEquals("Instant Pot", competitor.getBrand());
        assertEquals("DOUBLE INSIGHT INC", competitor.getSeller());
        assertEquals("Free order pickup", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("52361921", competitor.getSku());
        assertEquals("TURTLE BEACH&#174; Recon Chat Wired Gaming Headset for Xbox One", competitor.getName());
        assertEquals("19.95", competitor.getPrice().toString());
        assertEquals("Microsoft", competitor.getBrand());
        assertEquals("VOYETRA TURTLE BEACH INC", competitor.getSeller());
        assertEquals("Free order pickup", competitor.getShipment());
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
