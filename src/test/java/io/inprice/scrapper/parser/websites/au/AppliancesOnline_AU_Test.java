package io.inprice.scrapper.parser.websites.au;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.helpers.HttpClient;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AppliancesOnline_AU_Test {

    private final String SITE_NAME = "appliancesonline";
    private final String COUNTRY_CODE = "au";

    private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    private HttpClient httpClient = Mockito.mock(HttpClient.class);

    private final AppliancesOnline site =
        Mockito.spy(
            new AppliancesOnline(
                new Competitor()
            )
        );

    @Test
    public void test_product_1() {
        setMock(1);
        Competitor competitor = site.test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1562", competitor.getSku());
        assertEquals("Rinnai 6kg Dry-Soft 6 Natural Gas Dryer DRYSOFT6N", competitor.getName());
        assertEquals("1524.00", competitor.getPrice().toString());
        assertEquals("Rinnai", competitor.getBrand());
        assertEquals("Appliance Online", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Competitor competitor = site.test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("2708", competitor.getSku());
        assertEquals("InSinkErator 100 Evolution Food Waste Disposer", competitor.getName());
        assertEquals("978.00", competitor.getPrice().toString());
        assertEquals("InSinkErator", competitor.getBrand());
        assertEquals("Appliance Online", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Competitor competitor = site.test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("39819", competitor.getSku());
        assertEquals("WEBER 6579 Q Portable Cart for Baby Q Q 1000 & Q2000 Series 6579", competitor.getName());
        assertEquals("152.00", competitor.getPrice().toString());
        assertEquals("Weber", competitor.getBrand());
        assertEquals("Appliance Online", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Competitor competitor = site.test(null, httpClient);

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("56789", competitor.getSku());
        assertEquals("Dimplex DC18 5.3kW Portable Air Conditioner with Dehumidifier", competitor.getName());
        assertEquals("978.00", competitor.getPrice().toString());
        assertEquals("Dimplex", competitor.getBrand());
        assertEquals("Appliance Online", competitor.getSeller());
        assertEquals("Check delivery cost", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    private JSONObject getJsonDataFromFile(int no) {
        String data = null;
        try {
            InputStream is = Resources.getResource(String.format("websites/%s/%s_%d.json", COUNTRY_CODE, SITE_NAME, no)).openStream();
            data = CharStreams.toString(new InputStreamReader(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(data);
    }

    private void setMock(int no) {
        when(site.getJsonData()).thenReturn(getJsonDataFromFile(no));
        when(site.willHtmlBePulled()).thenReturn(false);

        when(mockResponse.getStatus()).thenReturn(0);
        when(mockResponse.getBody()).thenReturn(null);
        when(httpClient.get(anyString())).thenReturn(mockResponse);
    }

}