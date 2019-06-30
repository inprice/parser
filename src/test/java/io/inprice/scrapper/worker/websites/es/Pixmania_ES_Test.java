package io.inprice.scrapper.worker.websites.es;

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
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class Pixmania_ES_Test {

    private final String SITE_NAME = "pixmania";
    private final String COUNTRY_CODE = "es";

    private final Pixmania site = spy(new Pixmania(new Link()));
    private final HttpResponse response = mock(HttpResponse.class);

    public Pixmania_ES_Test() {
        PowerMockito.mockStatic(HttpClient.class);
    }

    @Test
    public void test_product_1() {
        setMock(1);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("LIBH60WH", link.getSku());
        assertEquals("Casco para Bici Inteligente Blanco (Luz,Intermitentes,Manos libres,Reproductor,Aviso caída) y Mando Multifunción", link.getName());
        assertEquals("99.99", link.getPrice().toString());
        assertEquals("Livall", link.getBrand());
        assertEquals("Ascendeo Iberia ES", link.getSeller());
        assertEquals("Free shipping: true", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("502773", link.getSku());
        assertEquals("Alpine 64 PLUS Procesador Enfriador", link.getName());
        assertEquals("30.95", link.getPrice().toString());
        assertEquals("ARCTIC", link.getBrand());
        assertEquals("PIXMANIA ESPANA", link.getSeller());
        assertEquals("Free shipping: true", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("VLVB34700B30", link.getSku());
        assertEquals("Cable HDMI de alta velocidad con conector HDMIEthe", link.getName());
        assertEquals("17.86", link.getPrice().toString());
        assertEquals("VALUELINE", link.getBrand());
        assertEquals("IberiaPC ES", link.getSeller());
        assertEquals("Free shipping: true", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("1022729", link.getSku());
        assertEquals("6 discos lavables", link.getName());
        assertEquals("28.95", link.getPrice().toString());
        assertEquals("PHILIPS AVENT", link.getBrand());
        assertEquals("PIXMANIA ESPANA", link.getSeller());
        assertEquals("Free shipping: true", link.getShipment());
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

        when(site.getUrl()).thenReturn("");
        when(site.getPayload()).thenReturn(new HashMap<>());
        when(site.willHtmlBePulled()).thenReturn(false);

        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(data);
        when(HttpClient.get(anyString(), anyMap())).thenReturn(response);
    }

}