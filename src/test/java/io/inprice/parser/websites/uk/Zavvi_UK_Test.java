package io.inprice.parser.websites.uk;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zavvi_UK_Test {

    private final String SITE_NAME = "zavvi";
    private final String COUNTRY_CODE = "uk";

    private final Zavvi site = new Zavvi(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("11852041", competitor.getSku());
        assertEquals("Meta Merch Star Wars Chewbacca Arm Mug", competitor.getName());
        assertEquals("7.99", competitor.getPrice().toString());
        assertEquals("Exquisite Gaming", competitor.getBrand());
        assertEquals("Zavvi", competitor.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("12071095", competitor.getSku());
        assertEquals("Marvel Pop! Advent Calendar (2019)", competitor.getName());
        assertEquals("49.99", competitor.getPrice().toString());
        assertEquals("Funko Pop! Vinyl", competitor.getBrand());
        assertEquals("Zavvi", competitor.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("11477837", competitor.getSku());
        assertEquals("Nintendo Retro NES Classically Trained Men's White T-Shirt", competitor.getName());
        assertEquals("14.99", competitor.getPrice().toString());
        assertEquals("Nintendo", competitor.getBrand());
        assertEquals("Zavvi", competitor.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("12183579", competitor.getSku());
        assertEquals("Sonic the Hedgehog BOOM8 Series PVC Figure Vol. 02 Sonic (8cm)", competitor.getName());
        assertEquals("24.99", competitor.getPrice().toString());
        assertEquals("First 4 Figures", competitor.getBrand());
        assertEquals("Zavvi", competitor.getSeller());
        assertEquals("Express Delivery* - if ordered before 11pm, delivered by courier next working day. *On selected items", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
