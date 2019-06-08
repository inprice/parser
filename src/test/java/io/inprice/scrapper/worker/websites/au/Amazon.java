package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Website;

@RunWith(JUnit4.class)
public class Amazon {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "au";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B07FTHCPDP", link.getSku());
        assertEquals("Antler 4227130015 Juno 2 4W Large Roller Case Suitcases (Hardside), Turquoise, 81 cm", link.getName());
        assertEquals("179.00", link.getPrice().toString());
        assertEquals("ANTLER", link.getBrand());
        assertEquals("Bags_To_Go", link.getSeller());
        assertEquals("+ FREE Delivery", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B072KMX18S", link.getSku());
        assertEquals("Emporio Armani Men's Quartz Watch Analog Display and Stainless Steel Strap, AR11068", link.getName());
        assertEquals("233.00", link.getPrice().toString());
        assertEquals("Emporio Armani", link.getBrand());
        assertEquals("Amazon AU", link.getSeller());
        assertEquals("& FREE Delivery, plus Free Returns See details and conditions", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B0755PDKCN", link.getSku());
        assertEquals("GoPro The Handler 2017 Version DVC Accessories, Orange", link.getName());
        assertEquals("46.34", link.getPrice().toString());
        assertEquals("GoPro", link.getBrand());
        assertEquals("Amazon US", link.getSeller());
        assertEquals("+ FREE Delivery", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B07G9ZGL9X", link.getSku());
        assertEquals("M&M's Milk Chocolate Party Size Bucket (640g) (Packaging may vary)", link.getName());
        assertEquals("9.28", link.getPrice().toString());
        assertEquals("M&M'S", link.getBrand());
        assertEquals("Amazon AU", link.getSeller());
        assertEquals("& Free Delivery on eligible orders See details and conditions", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
