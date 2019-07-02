package io.inprice.scrapper.worker.websites.it;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class VidaXL_IT_Test {

    private final String SITE_NAME = "vidaxl";
    private final String COUNTRY_CODE = "it";

    private final VidaXL site = spy(new VidaXL(new Link()));
    private final HttpResponse response = mock(HttpResponse.class);

    public VidaXL_IT_Test() {
        PowerMockito.mockStatic(HttpClient.class);
    }

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("41192", link.getSku());
        assertEquals("Programmatore timer irrigazione elettronico automatico per orto 1 via", link.getName());
        assertEquals("26.99", link.getPrice().toString());
        assertEquals("VidaXL", link.getBrand());
        assertEquals("vidaXL", link.getSeller());
        assertEquals("Tempo di spedizione : 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("140654", link.getSku());
        assertEquals("vidaXL Scrostatore Pneumatico ad Aghi", link.getName());
        assertEquals("32.99", link.getPrice().toString());
        assertEquals("VidaXL", link.getBrand());
        assertEquals("vidaXL", link.getSeller());
        assertEquals("Tempo di spedizione : 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(getJsonFromFile(3));
        when(HttpClient.get(anyString())).thenReturn(response);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("41296", link.getSku());
        assertEquals("Recinzione con traliccio estensibile di legno 180 x 100 cm", link.getName());
        assertEquals("19.10", link.getPrice().toString());
        assertEquals("VidaXL", link.getBrand());
        assertEquals("VidaXL", link.getSeller());
        assertEquals("Tempo di consegna: 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        when(response.getStatus()).thenReturn(200);
        when(response.getBody()).thenReturn(getJsonFromFile(4));
        when(HttpClient.get(anyString())).thenReturn(response);

        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("141679", link.getSku());
        assertEquals("vidaXL Specchio Traffico Convesso Nero Plastica PC per Esterni 30 cm", link.getName());
        assertEquals("16.00", link.getPrice().toString());
        assertEquals("VidaXL", link.getBrand());
        assertEquals("VidaXL", link.getSeller());
        assertEquals("Tempo di consegna: 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    private String getJsonFromFile(int no) {
        String data = null;
        try {
            InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no)).openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
