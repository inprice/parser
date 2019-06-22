package io.inprice.scrapper.worker.websites.au;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.HttpClient;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.class)
public class AppliancesOnline_AU_Test {

    private final String SITE_NAME = "appliancesonline";
    private final String COUNTRY_CODE = "au";

    private final AppliancesOnline site = spy(new AppliancesOnline(new Link()));
    private final HttpResponse response = mock(HttpResponse.class);

    public AppliancesOnline_AU_Test() {
        PowerMockito.mockStatic(HttpClient.class);

        when(site.willHtmlBePulled()).thenReturn(false);

        when(response.getStatus()).thenReturn(0);
        when(response.getBody()).thenReturn(null);
        when(HttpClient.get(anyString())).thenReturn(response);
    }

    @Test
    public void test_product_1() {
        when(site.getJsonData()).thenReturn(getJsonDataFromFile(1));

        Link link = site.test(null);

        assertEquals(Status.ACTIVE, link.getStatus());
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
        when(site.getJsonData()).thenReturn(getJsonDataFromFile(2));

        Link link = site.test(null);

        assertEquals(Status.ACTIVE, link.getStatus());
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
        when(site.getJsonData()).thenReturn(getJsonDataFromFile(3));

        Link link = site.test(null);

        assertEquals(Status.ACTIVE, link.getStatus());
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
        when(site.getJsonData()).thenReturn(getJsonDataFromFile(4));

        Link link = site.test(null);

        assertEquals(Status.ACTIVE, link.getStatus());
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

}
