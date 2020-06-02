package io.inprice.scrapper.worker.websites.es;

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
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Pixmania_ES_Test {

    private final String SITE_NAME = "pixmania";
    private final String COUNTRY_CODE = "es";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Pixmania site =
        Mockito.spy(
            new Pixmania(
                new Competitor()
            )
        );

    @Test
    public void test_product_1() {
        setMock(1);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("LIBH60WH", competitor.getSku());
        assertEquals("Casco para Bici Inteligente Blanco (Luz,Intermitentes,Manos libres,Reproductor,Aviso caída) y Mando Multifunción", competitor.getName());
        assertEquals("99.99", competitor.getPrice().toString());
        assertEquals("Livall", competitor.getBrand());
        assertEquals("Ascendeo Iberia ES", competitor.getSeller());
        assertEquals("Free shipping: true", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("502773", competitor.getSku());
        assertEquals("Alpine 64 PLUS Procesador Enfriador", competitor.getName());
        assertEquals("30.95", competitor.getPrice().toString());
        assertEquals("ARCTIC", competitor.getBrand());
        assertEquals("PIXMANIA ESPANA", competitor.getSeller());
        assertEquals("Free shipping: true", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("VLVB34700B30", competitor.getSku());
        assertEquals("Cable HDMI de alta velocidad con conector HDMIEthe", competitor.getName());
        assertEquals("17.86", competitor.getPrice().toString());
        assertEquals("VALUELINE", competitor.getBrand());
        assertEquals("IberiaPC ES", competitor.getSeller());
        assertEquals("Free shipping: true", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1022729", competitor.getSku());
        assertEquals("6 discos lavables", competitor.getName());
        assertEquals("28.95", competitor.getPrice().toString());
        assertEquals("PHILIPS AVENT", competitor.getBrand());
        assertEquals("PIXMANIA ESPANA", competitor.getSeller());
        assertEquals("Free shipping: true", competitor.getShipment());
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

        when(site.getUrl()).thenReturn("");
        when(site.getPayload()).thenReturn(new HashMap<>());
        when(site.willHtmlBePulled()).thenReturn(false);

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(data);
        when(httpClient.get(anyString(), anyMap())).thenReturn(mockResponse);
    }

}
