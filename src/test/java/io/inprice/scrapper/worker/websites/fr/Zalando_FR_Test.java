package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_FR_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1FI11A00Y-A14", competitor.getSku());
        assertEquals("RAY - Baskets basses", competitor.getName());
        assertEquals("99.95", competitor.getPrice().toString());
        assertEquals("Fila", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("PI922SA09-C12", competitor.getSku());
        assertEquals("Sweatshirt", competitor.getName());
        assertEquals("22.99", competitor.getPrice().toString());
        assertEquals("Pier One", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("L4221C0QS-K11", competitor.getSku());
        assertEquals("DARIANA ONE SHOULDER EVENING DRESS - Robe longue", competitor.getName());
        assertEquals("150.00", competitor.getPrice().toString());
        assertEquals("Lauren Ralph Lauren", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1SM44E01J-H11", competitor.getSku());
        assertEquals("VENTURE - Casque", competitor.getName());
        assertEquals("74.95", competitor.getPrice().toString());
        assertEquals("Smith Optics", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
