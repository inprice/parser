package io.inprice.scrapper.parser.websites.us;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Etsy_US_Test {

    private final String SITE_NAME = "etsy";
    private final String COUNTRY_CODE = "us";

    private final Etsy site = new Etsy(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("635397975", competitor.getSku());
        assertEquals("Wood wall Art, orange white art, reclaimed, 3D wood mosaic, modern wood art, abstract painting", competitor.getName());
        assertEquals("399.20", competitor.getPrice().toString());
        assertEquals("Kasia", competitor.getBrand());
        assertEquals("ArtGlamourSligo", competitor.getSeller());
        assertEquals("Ready to ship in 10 weeks. From Ireland. USD 105.98 shipping to Turkey.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("279237258", competitor.getSku());
        assertEquals("Purple pencil dress, Cocktail Stand collar Cheongsam Dress, Office Keyhole Fitted Wiggle Asian dress, Chinese Mandarin Dress TAVROVSKA", competitor.getName());
        assertEquals("69.89", competitor.getPrice().toString());
        assertEquals("Alex", competitor.getBrand());
        assertEquals("AStefanovych", competitor.getSeller());
        assertEquals("Ready to ship in 1–3 business days. From Ukraine. Free shipping to Turkey.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("462818186", competitor.getSku());
        assertEquals("Mint to Be Wedding Favor Stickers - 12 personalized favor labels, Tic Tac box stickers for mint favors, wedding or bridal shower gifts", competitor.getName());
        assertEquals("7.00", competitor.getPrice().toString());
        assertEquals("Brittany", competitor.getBrand());
        assertEquals("crakd", competitor.getSeller());
        assertEquals("Ready to ship in 2 business days. From United States. USD 14.35 shipping to Turkey.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("717993801", competitor.getSku());
        assertEquals("50pcs Silver Lava Rock Natural Gemstone Beads 7-8mm Round 16 Inches Strand", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("SBBeadsAndCrafts", competitor.getBrand());
        assertEquals("SBBeadsAndCrafts", competitor.getSeller());
        assertEquals("Ready to ship in 3–5 business days. From United States. USD 18.90 shipping to Turkey.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
