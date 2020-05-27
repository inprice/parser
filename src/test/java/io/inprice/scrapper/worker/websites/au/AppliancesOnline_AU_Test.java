package io.inprice.scrapper.worker.websites.au;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
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
                new Link()
            )
        );

    @Test
    public void test_product_1() {
        setMock(1);
        Link link = site.test(null, httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1562", link.getSku());
        assertEquals("Rinnai 6kg Dry-Soft 6 Natural Gas Dryer DRYSOFT6N", link.getName());
        assertEquals("1524.00", link.getPrice().toString());
        assertEquals("Rinnai", link.getBrand());
        assertEquals("Appliance Online", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        setMock(2);
        Link link = site.test(null, httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("2708", link.getSku());
        assertEquals("InSinkErator 100 Evolution Food Waste Disposer", link.getName());
        assertEquals("978.00", link.getPrice().toString());
        assertEquals("InSinkErator", link.getBrand());
        assertEquals("Appliance Online", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        setMock(3);
        Link link = site.test(null, httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("39819", link.getSku());
        assertEquals("WEBER 6579 Q Portable Cart for Baby Q Q 1000 & Q2000 Series 6579", link.getName());
        assertEquals("152.00", link.getPrice().toString());
        assertEquals("Weber", link.getBrand());
        assertEquals("Appliance Online", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        setMock(4);
        Link link = site.test(null, httpClient);

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("56789", link.getSku());
        assertEquals("Dimplex DC18 5.3kW Portable Air Conditioner with Dehumidifier", link.getName());
        assertEquals("978.00", link.getPrice().toString());
        assertEquals("Dimplex", link.getBrand());
        assertEquals("Appliance Online", link.getSeller());
        assertEquals("Check delivery cost", link.getShipment());
        assertNull(link.getSpecList());
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