package io.inprice.scrapper.worker.websites.it;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class VidaXL_IT_Test {

    private final String SITE_NAME = "vidaxl";
    private final String COUNTRY_CODE = "it";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    @Spy
    private final VidaXL site =
        Mockito.spy(
            new VidaXL(
                new Competitor()
            )
        );

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("41192", competitor.getSku());
        assertEquals("Programmatore timer irrigazione elettronico automatico per orto 1 via", competitor.getName());
        assertEquals("26.99", competitor.getPrice().toString());
        assertEquals("vidaXL", competitor.getBrand());
        assertEquals("vidaXL", competitor.getSeller());
        assertEquals("Tempo di spedizione : 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("140654", competitor.getSku());
        assertEquals("vidaXL Scrostatore Pneumatico ad Aghi", competitor.getName());
        assertEquals("32.99", competitor.getPrice().toString());
        assertEquals("vidaXL", competitor.getBrand());
        assertEquals("vidaXL", competitor.getSeller());
        assertEquals("Tempo di spedizione : 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(getJsonFromFile(3));
        when(httpClient.get(anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("41296", competitor.getSku());
        assertEquals("Recinzione con traliccio estensibile di legno 180 x 100 cm", competitor.getName());
        assertEquals("19.10", competitor.getPrice().toString());
        assertEquals("VidaXL", competitor.getBrand());
        assertEquals("VidaXL", competitor.getSeller());
        assertEquals("Tempo di consegna: 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(getJsonFromFile(4));
        when(httpClient.get(anyString())).thenReturn(mockResponse);

        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("141679", competitor.getSku());
        assertEquals("vidaXL Specchio Traffico Convesso Nero Plastica PC per Esterni 30 cm", competitor.getName());
        assertEquals("16.00", competitor.getPrice().toString());
        assertEquals("VidaXL", competitor.getBrand());
        assertEquals("VidaXL", competitor.getSeller());
        assertEquals("Tempo di consegna: 6 giorni lavorativi. Consegna e Reso gratuti. Venduto da: vidaXL", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
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
