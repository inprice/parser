package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Ebay_AU_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "au";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("253639280508", link.getSku());
        assertEquals("Milano Deluxe 3pc ABS Luggage Suitcase Luxury Hard Case Shockproof Travel Set", link.getName());
        assertEquals("99.95", link.getPrice().toString());
        assertEquals("Milano", link.getBrand());
        assertEquals("grouptwowarehouse", link.getSeller());
        assertEquals("May not post to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("291875944966", link.getSku());
        assertEquals("NEW Peter Thomas Roth Max Anti-Shine Mattifying Gel 30ml Womens Skin Care", link.getName());
        assertEquals("41.97", link.getPrice().toString());
        assertEquals("Peter Thomas Roth", link.getBrand());
        assertEquals("the_beauty_club_au", link.getSeller());
        assertEquals("Doesn't post to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("254255431988", link.getSku());
        assertEquals("140340 vidaXL Auger Ground Drill Orange", link.getName());
        assertEquals("195.97", link.getPrice().toString());
        assertEquals("vidaXL", link.getBrand());
        assertEquals("comebuy-uk", link.getSeller());
        assertEquals("Doesn't post to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("283038845115", link.getSku());
        assertEquals("MEN'S TROUSERS DIAMOND mod. CAPRI DARK GREY CHECK CASUAL COTTON", link.getName());
        assertEquals("38.13", link.getPrice().toString());
        assertEquals("Diamond Collezioni", link.getBrand());
        assertEquals("gi-store1", link.getSeller());
        assertEquals("AU $21.18 Australia Post Air Mail Parcel", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
