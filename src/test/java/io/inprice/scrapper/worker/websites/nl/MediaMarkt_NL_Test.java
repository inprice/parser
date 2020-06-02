package io.inprice.scrapper.worker.websites.nl;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaMarkt_NL_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.MediaMarkt(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1070699", competitor.getSku());
        assertEquals("HAMA USB 2.0 verlengkabel 1 ster 0,25m", competitor.getName());
        assertEquals("6.99", competitor.getPrice().toString());
        assertEquals("HAMA", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Gratis bezorging vanaf € 20", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1555572", competitor.getSku());
        assertEquals("ZANUSSI ZGH65414XS", competitor.getName());
        assertEquals("299.00", competitor.getPrice().toString());
        assertEquals("ZANUSSI", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Gratis bezorging", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1484859", competitor.getSku());
        assertEquals("LOGITECH POP Add-on Home Switch Grijs", competitor.getName());
        assertEquals("29.00", competitor.getPrice().toString());
        assertEquals("LOGITECH", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Incl. btw excl. verzendkosten", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1399691", competitor.getSku());
        assertEquals("Sarah McLachlan - Surfacing | Vinyl", competitor.getName());
        assertEquals("22.99", competitor.getPrice().toString());
        assertEquals("BERTUS DISTRIBUTIE BERT", competitor.getBrand());
        assertEquals("Media Markt", competitor.getSeller());
        assertEquals("Gratis bezorging", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
