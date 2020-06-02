package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NewLook_UK_Test {

    private final String SITE_NAME = "newlook";
    private final String COUNTRY_CODE = "uk";

    private final NewLook site = new NewLook(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("623032293", competitor.getSku());
        assertEquals("Gold Wood Money Box and frame", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("New Look", competitor.getBrand());
        assertEquals("NewLook", competitor.getSeller());
        assertEquals("Free Delivery*", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("629261276", competitor.getSku());
        assertEquals("Bright Pink Satin Tiger Jacquard Midi Dress", competitor.getName());
        assertEquals("27.99", competitor.getPrice().toString());
        assertEquals("New Look", competitor.getBrand());
        assertEquals("NewLook", competitor.getSeller());
        assertEquals("Free Delivery*", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("616980110", competitor.getSku());
        assertEquals("White Side Stripe Lace Up Trainers", competitor.getName());
        assertEquals("11.24", competitor.getPrice().toString());
        assertEquals("New Look", competitor.getBrand());
        assertEquals("NewLook", competitor.getSeller());
        assertEquals("Free Delivery*", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("612265801", competitor.getSku());
        assertEquals("Black Leather-Look Chain Strap Utility Bum Bag", competitor.getName());
        assertEquals("12.99", competitor.getPrice().toString());
        assertEquals("New Look", competitor.getBrand());
        assertEquals("NewLook", competitor.getSeller());
        assertEquals("Free Delivery*", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
