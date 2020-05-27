package io.inprice.scrapper.worker.websites.uk;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Asda_UK_Test {

    private final String SITE_NAME = "asda";
    private final String COUNTRY_CODE = "uk";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final Asda site =
        Mockito.spy(
            new Asda(
                new Link()
            )
        );

    @Test
    public void test_product_1() {
        final String prodId = "1000034704516";

        setMock(1, prodId);
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
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

        when(mockResponse.getStatus()).thenReturn(200);
        when(mockResponse.getBody()).thenReturn(data);
        when(httpClient.get(anyString())).thenReturn(mockResponse);
    }

}
