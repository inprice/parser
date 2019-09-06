package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_NL_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B01HPVH7HA", link.getSku());
        assertEquals("The 80/20 Principle: The Secret of Achieving More with Less: Updated 20th anniversary edition of the productivity and business classic (English Edition)", link.getName());
        assertEquals("0.99", link.getPrice().toString());
        assertEquals("Richard Koch (auteur)", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B00ZAT8VS4", link.getSku());
        assertEquals("Platform Revolution: How Networked Markets Are Transforming the Economyand How to Make Them Work for You (English Edition)", link.getName());
        assertEquals("10.88", link.getPrice().toString());
        assertEquals("Geoffrey G. Parker (auteur), Marshall W. Van Alstyne (auteur), Sangeet Paul Choudary (auteur) & 0 meer", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B00JQ3FN7M", link.getSku());
        assertEquals("Make It Stick (English Edition)", link.getName());
        assertEquals("21.24", link.getPrice().toString());
        assertEquals("Peter C. Brown (auteur)", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("B07DLBW8ND", link.getSku());
        assertEquals("De jongen in de sneeuw", link.getName());
        assertEquals("4.99", link.getPrice().toString());
        assertEquals("Samuel Bjork (auteur), Renée Vink (Vertaler)", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
