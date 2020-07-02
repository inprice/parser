package io.inprice.parser.websites.uk;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.helpers.HttpClient;
import io.inprice.parser.websites.Helpers;
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
                new Competitor()
            )
        );

    @Test
    public void test_product_1() {
        final String prodId = "1000034704516";

        setMock(1, prodId);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(prodId, competitor.getSku());
        assertEquals("Gran Lomo Malbec", competitor.getName());
        assertEquals("5.00", competitor.getPrice().toString());
        assertEquals("Gran Lomo", competitor.getBrand());
        assertEquals("Asda", competitor.getSeller());
        assertEquals("In-store pickup.", competitor.getShipment());
    }

    @Test
    public void test_product_2() {
        final String prodId = "910000317601";

        setMock(2, prodId);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(prodId, competitor.getSku());
        assertEquals("Purina ONE Adult Dry Cat Food Salmon and Wholegrain", competitor.getName());
        assertEquals("11.00", competitor.getPrice().toString());
        assertEquals("Purina ONE", competitor.getBrand());
        assertEquals("Asda", competitor.getSeller());
        assertEquals("In-store pickup.", competitor.getShipment());
    }

    @Test
    public void test_product_3() {
        final String prodId = "1000122184231";

        setMock(3, prodId);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(prodId, competitor.getSku());
        assertEquals("Prince Chocolate Sandwich Biscuits", competitor.getName());
        assertEquals("1.00", competitor.getPrice().toString());
        assertEquals("Prince", competitor.getBrand());
        assertEquals("Asda", competitor.getSeller());
        assertEquals("In-store pickup.", competitor.getShipment());
    }

    @Test
    public void test_product_4() {
        final String prodId = "910001116620";

        setMock(4, prodId);
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4), httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals(prodId, competitor.getSku());
        assertEquals("Oral-B Pro 600 White & Clean Rechargeable Electric Toothbrush", competitor.getName());
        assertEquals("15.00", competitor.getPrice().toString());
        assertEquals("Oral-B", competitor.getBrand());
        assertEquals("Asda", competitor.getSeller());
        assertEquals("In-store pickup.", competitor.getShipment());
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
