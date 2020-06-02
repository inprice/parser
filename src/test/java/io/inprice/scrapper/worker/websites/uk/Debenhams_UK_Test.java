package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Debenhams_UK_Test {

    private final String SITE_NAME = "debenhams";
    private final String COUNTRY_CODE = "uk";

    private final Debenhams site = new Debenhams(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3310014132", competitor.getSku());
        assertEquals("Black non-stick aluminium 'Gourmet' 5 piece pan set", competitor.getName());
        assertEquals("68.00", competitor.getPrice().toString());
        assertEquals("Tefal", competitor.getBrand());
        assertEquals("Debenhams", competitor.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1610104181", competitor.getSku());
        assertEquals("Black Embroidered Mesh Satin Brazilian Knickers", competitor.getName());
        assertEquals("10.00", competitor.getPrice().toString());
        assertEquals("Reger by Janet Reger", competitor.getBrand());
        assertEquals("Debenhams", competitor.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("61131_105548", competitor.getSku());
        assertEquals("Pleasure Dome Topkapi Opal Designer Wallpaper", competitor.getName());
        assertEquals("12.50", competitor.getPrice().toString());
        assertEquals("Laurence Llewelyn-Bowen", competitor.getBrand());
        assertEquals("Debenhams", competitor.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1710104267", competitor.getSku());
        assertEquals("My First Talking Ted", competitor.getName());
        assertEquals("8.80", competitor.getPrice().toString());
        assertEquals("Early Learning Centre", competitor.getBrand());
        assertEquals("Debenhams", competitor.getSeller());
        assertEquals("FREE Standard Delivery on orders £50 or over", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
