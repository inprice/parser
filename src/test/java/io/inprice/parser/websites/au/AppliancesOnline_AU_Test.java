package io.inprice.parser.websites.au;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.HttpClient;
import kong.unirest.HttpResponse;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AppliancesOnline_AU_Test {

  private HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
  private HttpClient httpClient = Mockito.mock(HttpClient.class);

  private final AppliancesOnline site;

  public AppliancesOnline_AU_Test() {
    this.site = Mockito.spy(new AppliancesOnline());
  }

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

  private void setMock(int no) {
    //when(site.getJsonData()).thenReturn(getJsonDataFromFile(no));
    when(site.willHtmlBePulled()).thenReturn(false);

    when(mockResponse.getStatus()).thenReturn(0);
    when(mockResponse.getBody()).thenReturn(null);
    when(httpClient.get(anyString())).thenReturn(mockResponse);
  }

  /*
  private JSONObject getJsonDataFromFile(int no) {
    return new JSONObject(
      Helpers.readFile(
        String.format("websites/%s/%s_%d.json", site.getCountry().getCode(), site.getSiteName(), no)
      )
    );
  }
  */

}