package io.inprice.parser.websites.au;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_AU_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "au";

    private final Website site = new io.inprice.parser.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("253639280508", competitor.getSku());
        assertEquals("Milano Deluxe 3pc ABS Luggage Suitcase Luxury Hard Case Shockproof Travel Set", competitor.getName());
        assertEquals("99.95", competitor.getPrice().toString());
        assertEquals("Milano", competitor.getBrand());
        assertEquals("grouptwowarehouse", competitor.getSeller());
        assertEquals("May not post to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("291875944966", competitor.getSku());
        assertEquals("NEW Peter Thomas Roth Max Anti-Shine Mattifying Gel 30ml Womens Skin Care", competitor.getName());
        assertEquals("41.97", competitor.getPrice().toString());
        assertEquals("Peter Thomas Roth", competitor.getBrand());
        assertEquals("the_beauty_club_au", competitor.getSeller());
        assertEquals("Doesn't post to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("254255431988", competitor.getSku());
        assertEquals("140340 vidaXL Auger Ground Drill Orange", competitor.getName());
        assertEquals("195.97", competitor.getPrice().toString());
        assertEquals("vidaXL", competitor.getBrand());
        assertEquals("comebuy-uk", competitor.getSeller());
        assertEquals("Doesn't post to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("283038845115", competitor.getSku());
        assertEquals("MEN'S TROUSERS DIAMOND mod. CAPRI DARK GREY CHECK CASUAL COTTON", competitor.getName());
        assertEquals("38.13", competitor.getPrice().toString());
        assertEquals("Diamond Collezioni", competitor.getBrand());
        assertEquals("gi-store1", competitor.getSeller());
        assertEquals("AU $21.18 Australia Post Air Mail Parcel", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
