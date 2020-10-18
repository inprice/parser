package io.inprice.parser.websites.ca;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_CA_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "ca";

    private final Website site = new io.inprice.parser.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B00006J6WX", link.getSku());
        assertEquals("Brita Water Filter Pitcher Advanced Replacement Filters, 1 Count", link.getName());
        assertEquals("7.48", link.getPrice().toString());
        assertEquals("Brita", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("& FREE Shipping on orders over CDN$ 35. Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B00LH3DMUO", link.getSku());
        assertEquals("AmazonBasics AAA Performance Alkaline Batteries (36 Count)", link.getName());
        assertEquals("16.16", link.getPrice().toString());
        assertEquals("AmazonBasics", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("& FREE Shipping on orders over CDN$ 35. Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B00HYMXL8K", link.getSku());
        assertEquals("South Shore Furniture Cuddly Changing Table with Removable Changing Station, Pure White", link.getName());
        assertEquals("145.00", link.getPrice().toString());
        assertEquals("South Shore Furniture", link.getBrand());
        assertEquals("South Shore Furniture Canada", link.getSeller());
        assertEquals("& FREE Shipping. Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07QHWTVFT", link.getSku());
        assertEquals("Lifelike Toys for Women - Large Size - 36 Speeds - USB Rechargeable - Perfect Size & Party Gifts", link.getName());
        assertEquals("28.80", link.getPrice().toString());
        assertEquals("LGAHENG", link.getBrand());
        assertEquals("LIMEISANMO", link.getSeller());
        assertEquals("+ FREE SHIPPING", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
