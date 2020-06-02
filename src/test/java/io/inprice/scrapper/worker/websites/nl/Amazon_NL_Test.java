package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_NL_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B01HPVH7HA", competitor.getSku());
        assertEquals("The 80/20 Principle: The Secret of Achieving More with Less: Updated 20th anniversary edition of the productivity and business classic (English Edition)", competitor.getName());
        assertEquals("0.99", competitor.getPrice().toString());
        assertEquals("Richard Koch (auteur)", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00ZAT8VS4", competitor.getSku());
        assertEquals("Platform Revolution: How Networked Markets Are Transforming the Economyand How to Make Them Work for You (English Edition)", competitor.getName());
        assertEquals("10.88", competitor.getPrice().toString());
        assertEquals("Geoffrey G. Parker (auteur), Marshall W. Van Alstyne (auteur), Sangeet Paul Choudary (auteur) & 0 meer", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00JQ3FN7M", competitor.getSku());
        assertEquals("Make It Stick (English Edition)", competitor.getName());
        assertEquals("21.24", competitor.getPrice().toString());
        assertEquals("Peter C. Brown (auteur)", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07DLBW8ND", competitor.getSku());
        assertEquals("De jongen in de sneeuw", competitor.getName());
        assertEquals("4.99", competitor.getPrice().toString());
        assertEquals("Samuel Bjork (auteur), Renée Vink (Vertaler)", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Laat het op je Kindle of een ander apparaat leveren", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
