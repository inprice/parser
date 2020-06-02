package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_DE_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("370503654868", competitor.getSku());
        assertEquals("SURPLUS DIVISION CARGO SHORT 7FARBEN XS-7XL U.S. Army Bermuda Rider Shorts Walk", competitor.getName());
        assertEquals("29.90", competitor.getPrice().toString());
        assertEquals("Surplus Raw Vintage", competitor.getBrand());
        assertEquals("urbandreamz_ltd", competitor.getSeller());
        assertEquals("EUR 34,90 Standardversand", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("254158224125", competitor.getSku());
        assertEquals("Luxus Gold Panzerkette Armband Set Herrenkette Edelstahl 18 Karat vergoldet 60cm", competitor.getName());
        assertEquals("32.39", competitor.getPrice().toString());
        assertEquals("Beyalyjwls", competitor.getBrand());
        assertEquals("beyalyjwls", competitor.getSeller());
        assertEquals("Möglicherweise kein Versand nach Türkei", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("143052381051", competitor.getSku());
        assertEquals("adidas Performance Core 18 Kapuzenpullover Herren NEU", competitor.getName());
        assertEquals("24.95", competitor.getPrice().toString());
        assertEquals("adidas Performance", competitor.getBrand());
        assertEquals("outfitter-shop", competitor.getSeller());
        assertEquals("Kein Versand nach Türkei", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("292503186932", competitor.getSku());
        assertEquals("Einhell GE-HH 18 Li T Kit Akku-Heckenschere 3.0 Ah Power-X-Change Strauchschere", competitor.getName());
        assertEquals("99.95", competitor.getPrice().toString());
        assertEquals("Einhell", competitor.getBrand());
        assertEquals("elektro-himmel", competitor.getSeller());
        assertEquals("Kein Versand nach Türkei", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
