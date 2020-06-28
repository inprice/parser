package io.inprice.scrapper.parser.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_TR_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("302965837951", competitor.getSku());
        assertEquals("Nextbase 312G Dash Cam 1080P 2.7\" LED Car Recorder Night Vision", competitor.getName());
        assertEquals("49.95", competitor.getPrice().toString());
        assertEquals("NEXTBASE", competitor.getBrand());
        assertEquals("velocityelectronics", competitor.getSeller());
        assertEquals("May not post to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("282124090417", competitor.getSku());
        assertEquals("VonShef Deep Fat Fryer 1.5 Litre Chip Pan Basket Non Stick Oil Fry 900W Compact", competitor.getName());
        assertEquals("18.99", competitor.getPrice().toString());
        assertEquals("VonShef", competitor.getBrand());
        assertEquals("domu-uk", competitor.getSeller());
        assertEquals("Doesn't post to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("312192267322", competitor.getSku());
        assertEquals("Levis 2 Pack Trunk Short Briefs 200SF Vintage HeatherMens Birthday", competitor.getName());
        assertEquals("10.99", competitor.getPrice().toString());
        assertEquals("Levis", competitor.getBrand());
        assertEquals("underworldunderwear", competitor.getSeller());
        assertEquals("£9.99 Royal Mail International Tracked", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("362401070603", competitor.getSku());
        assertEquals("Lava KAHULA Portable Bluetooth Soundbar Speaker With Rechargable Battery", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("Lava", competitor.getBrand());
        assertEquals("3monkeys", competitor.getSeller());
        assertEquals("May not post to Turkey", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
