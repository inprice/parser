package io.inprice.scrapper.parser.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_NL_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.Bonprix(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("97447795_29806865", competitor.getSku());
        assertEquals("Sweatbermuda", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("bpc bonprix collection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 4,95 per bestelling Meer informatie >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("94977395_26495747", competitor.getSku());
        assertEquals("Dekbedovertrek «Aap»", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("bpc living", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 4,95 per bestelling Meer informatie >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("90555595_26598631", competitor.getSku());
        assertEquals("Halskettingen (3-dlg. set)", competitor.getName());
        assertEquals("12.99", competitor.getPrice().toString());
        assertEquals("RAINBOW", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("€ 4,95 per bestelling Meer informatie >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
