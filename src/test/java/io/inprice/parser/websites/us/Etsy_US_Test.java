package io.inprice.parser.websites.us;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Etsy_US_Test {

    private final String SITE_NAME = "etsy";
    private final String COUNTRY_CODE = "us";

    private final Etsy site = new Etsy(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("635397975", link.getSku());
        assertEquals("Wood wall Art, orange white art, reclaimed, 3D wood mosaic, modern wood art, abstract painting", link.getName());
        assertEquals("399.20", link.getPrice().toString());
        assertEquals("Kasia", link.getBrand());
        assertEquals("ArtGlamourSligo", link.getSeller());
        assertEquals("Ready to ship in 10 weeks. From Ireland. USD 105.98 shipping to Turkey.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("279237258", link.getSku());
        assertEquals("Purple pencil dress, Cocktail Stand collar Cheongsam Dress, Office Keyhole Fitted Wiggle Asian dress, Chinese Mandarin Dress TAVROVSKA", link.getName());
        assertEquals("69.89", link.getPrice().toString());
        assertEquals("Alex", link.getBrand());
        assertEquals("AStefanovych", link.getSeller());
        assertEquals("Ready to ship in 1–3 business days. From Ukraine. Free shipping to Turkey.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("462818186", link.getSku());
        assertEquals("Mint to Be Wedding Favor Stickers - 12 personalized favor labels, Tic Tac box stickers for mint favors, wedding or bridal shower gifts", link.getName());
        assertEquals("7.00", link.getPrice().toString());
        assertEquals("Brittany", link.getBrand());
        assertEquals("crakd", link.getSeller());
        assertEquals("Ready to ship in 2 business days. From United States. USD 14.35 shipping to Turkey.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("717993801", link.getSku());
        assertEquals("50pcs Silver Lava Rock Natural Gemstone Beads 7-8mm Round 16 Inches Strand", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("SBBeadsAndCrafts", link.getBrand());
        assertEquals("SBBeadsAndCrafts", link.getSeller());
        assertEquals("Ready to ship in 3–5 business days. From United States. USD 18.90 shipping to Turkey.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
