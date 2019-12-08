package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_TR_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("302965837951", link.getSku());
        assertEquals("Nextbase 312G Dash Cam 1080P 2.7\" LED Car Recorder Night Vision", link.getName());
        assertEquals("49.95", link.getPrice().toString());
        assertEquals("NEXTBASE", link.getBrand());
        assertEquals("velocityelectronics", link.getSeller());
        assertEquals("May not post to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("282124090417", link.getSku());
        assertEquals("VonShef Deep Fat Fryer 1.5 Litre Chip Pan Basket Non Stick Oil Fry 900W Compact", link.getName());
        assertEquals("18.99", link.getPrice().toString());
        assertEquals("VonShef", link.getBrand());
        assertEquals("domu-uk", link.getSeller());
        assertEquals("Doesn't post to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("312192267322", link.getSku());
        assertEquals("Levis 2 Pack Trunk Short Briefs 200SF Vintage HeatherMens Birthday", link.getName());
        assertEquals("10.99", link.getPrice().toString());
        assertEquals("Levis", link.getBrand());
        assertEquals("underworldunderwear", link.getSeller());
        assertEquals("£9.99 Royal Mail International Tracked", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("362401070603", link.getSku());
        assertEquals("Lava KAHULA Portable Bluetooth Soundbar Speaker With Rechargable Battery", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("Lava", link.getBrand());
        assertEquals("3monkeys", link.getSeller());
        assertEquals("May not post to Turkey", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
