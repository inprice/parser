package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Bonprix_DE_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("96091695_27838420", competitor.getSku());
        assertEquals("Solarleuchte \"Leuchtturm\"", competitor.getName());
        assertEquals("19.99", competitor.getPrice().toString());
        assertEquals("bpc living", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("92556195_29973862", competitor.getSku());
        assertEquals("Brazil Slip (2er-Pack)", competitor.getName());
        assertEquals("14.98", competitor.getPrice().toString());
        assertEquals("BODYFLIRT", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("95378681_29519547", competitor.getSku());
        assertEquals("Outdoor Boot", competitor.getName());
        assertEquals("29.99", competitor.getPrice().toString());
        assertEquals("bpc bonprix collection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("95016895_29852337", competitor.getSku());
        assertEquals("Mädchen Shirtkleid mit Volant", competitor.getName());
        assertEquals("12.99", competitor.getPrice().toString());
        assertEquals("bpc bonprix collection", competitor.getBrand());
        assertEquals("Bonprix", competitor.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
