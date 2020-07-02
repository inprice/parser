package io.inprice.parser.websites.ca;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_CA_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "ca";

    private final Website site = new io.inprice.parser.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00006J6WX", competitor.getSku());
        assertEquals("Brita Water Filter Pitcher Advanced Replacement Filters, 1 Count", competitor.getName());
        assertEquals("7.48", competitor.getPrice().toString());
        assertEquals("Brita", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("& FREE Shipping on orders over CDN$ 35. Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00LH3DMUO", competitor.getSku());
        assertEquals("AmazonBasics AAA Performance Alkaline Batteries (36 Count)", competitor.getName());
        assertEquals("16.16", competitor.getPrice().toString());
        assertEquals("AmazonBasics", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("& FREE Shipping on orders over CDN$ 35. Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00HYMXL8K", competitor.getSku());
        assertEquals("South Shore Furniture Cuddly Changing Table with Removable Changing Station, Pure White", competitor.getName());
        assertEquals("145.00", competitor.getPrice().toString());
        assertEquals("South Shore Furniture", competitor.getBrand());
        assertEquals("South Shore Furniture Canada", competitor.getSeller());
        assertEquals("& FREE Shipping. Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07QHWTVFT", competitor.getSku());
        assertEquals("Lifelike Toys for Women - Large Size - 36 Speeds - USB Rechargeable - Perfect Size & Party Gifts", competitor.getName());
        assertEquals("28.80", competitor.getPrice().toString());
        assertEquals("LGAHENG", competitor.getBrand());
        assertEquals("LIMEISANMO", competitor.getSeller());
        assertEquals("+ FREE SHIPPING", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
