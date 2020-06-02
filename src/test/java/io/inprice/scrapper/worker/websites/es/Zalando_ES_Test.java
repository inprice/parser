package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_ES_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("C1852F00Z-Q11", competitor.getSku());
        assertEquals("Monedero", competitor.getName());
        assertEquals("38.95", competitor.getPrice().toString());
        assertEquals("Calvin Klein", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("CL612M00S-B11", competitor.getSku());
        assertEquals("WALLABEE - Zapatos de vestir", competitor.getName());
        assertEquals("135.95", competitor.getPrice().toString());
        assertEquals("Clarks Originals", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("EV451Q00W-G11", competitor.getSku());
        assertEquals("Mochila", competitor.getName());
        assertEquals("20.25", competitor.getPrice().toString());
        assertEquals("Even&Odd", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("GP021C0BY-A11", competitor.getSku());
        assertEquals("EYELET - Vestido camisero", competitor.getName());
        assertEquals("48.95", competitor.getPrice().toString());
        assertEquals("GAP", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Envío estándar: gratuito entrega en 3-6 días laborables Envío exprés: 7,95 €  entrega en 1-2 días laborables", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
