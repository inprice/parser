package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.meta.LinkStatus;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BestBuy_CA_Test {

    private final String SITE_NAME = "bestbuy";
    private final String COUNTRY_CODE = "ca";

    private final Website site = new BestBuy(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("10750964", link.getSku());
        assertEquals("NIKON D7500 DSLR Camera with 18-140mm ED VR Lens Kit", link.getName());
        assertEquals("1399.99", link.getPrice().toString());
        assertEquals("NIKON", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("13548749", link.getSku());
        assertEquals("Marshall Minor II In-Ear Bluetooth Headphones - Black", link.getName());
        assertEquals("179.99", link.getPrice().toString());
        assertEquals("MARSHALL", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("10389173", link.getSku());
        assertEquals("Philips Digital Air Fryer - 0.8 kg - Black", link.getName());
        assertEquals("179.99", link.getPrice().toString());
        assertEquals("PHILIPS", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("10440129", link.getSku());
        assertEquals("CorLiving Patio Umbrella Base - Dark Grey", link.getName());
        assertEquals("49.99", link.getPrice().toString());
        assertEquals("CORLIVING", link.getBrand());
        assertEquals("Best Buy", link.getSeller());
        assertEquals("Sold and shipped by Best Buy", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
