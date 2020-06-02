package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaMarkt_DE_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.de.MediaMarkt(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("2251315", competitor.getSku());
        assertEquals("LELO EARL GOLD Analplug", competitor.getName());
        assertEquals("1399.00", competitor.getPrice().toString());
        assertEquals("LELO", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Kostenloser Versand", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1255714", competitor.getSku());
        assertEquals("KOPP 120913004 TSD Steckdosenleiste", competitor.getName());
        assertEquals("4.99", competitor.getPrice().toString());
        assertEquals("KOPP", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Versandkosten 1.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("2183219", competitor.getSku());
        assertEquals("GOPRO 3661-164, Dualladegerät + Akku, GoPro HERO5 Black, GoPro HERO6 Black, GoPro Hero7 Black, Schwarz", competitor.getName());
        assertEquals("57.99", competitor.getPrice().toString());
        assertEquals("GOPRO", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Versandkosten 1.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.NOT_AVAILABLE, competitor.getStatus());
        assertEquals("2519798", competitor.getSku());
        assertEquals("Kiss - Kissworld-The Best Of Kiss (2LP) [Vinyl]", competitor.getName());
        assertEquals("22.99", competitor.getPrice().toString());
        assertEquals("MERCURY", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Versandkosten 1.99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
