package io.inprice.scrapper.worker.websites.ca;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BestBuy_CA_Test {

    private final String SITE_NAME = "bestbuy";
    private final String COUNTRY_CODE = "ca";

    private final Website site = new BestBuy(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("10750964", competitor.getSku());
        assertEquals("NIKON D7500 DSLR Camera with 18-140mm ED VR Lens Kit", competitor.getName());
        assertEquals("1399.99", competitor.getPrice().toString());
        assertEquals("NIKON", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("13548749", competitor.getSku());
        assertEquals("Marshall Minor II In-Ear Bluetooth Headphones - Black", competitor.getName());
        assertEquals("179.99", competitor.getPrice().toString());
        assertEquals("MARSHALL", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("10389173", competitor.getSku());
        assertEquals("Philips Digital Air Fryer - 0.8 kg - Black", competitor.getName());
        assertEquals("179.99", competitor.getPrice().toString());
        assertEquals("PHILIPS", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("10440129", competitor.getSku());
        assertEquals("CorLiving Patio Umbrella Base - Dark Grey", competitor.getName());
        assertEquals("49.99", competitor.getPrice().toString());
        assertEquals("CORLIVING", competitor.getBrand());
        assertEquals("Best Buy", competitor.getSeller());
        assertEquals("Sold and shipped by Best Buy", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
