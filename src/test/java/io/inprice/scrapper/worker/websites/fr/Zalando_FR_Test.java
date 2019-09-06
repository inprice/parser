package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_FR_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1FI11A00Y-A14", link.getSku());
        assertEquals("RAY - Baskets basses", link.getName());
        assertEquals("99.95", link.getPrice().toString());
        assertEquals("Fila", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("PI922SA09-C12", link.getSku());
        assertEquals("Sweatshirt", link.getName());
        assertEquals("22.99", link.getPrice().toString());
        assertEquals("Pier One", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("L4221C0QS-K11", link.getSku());
        assertEquals("DARIANA ONE SHOULDER EVENING DRESS - Robe longue", link.getName());
        assertEquals("150.00", link.getPrice().toString());
        assertEquals("Lauren Ralph Lauren", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1SM44E01J-H11", link.getSku());
        assertEquals("VENTURE - Casque", link.getName());
        assertEquals("74.95", link.getPrice().toString());
        assertEquals("Smith Optics", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Livraison Standard gratuite 3-6 jours ouvrables Livraison Express 9,95 € 1-2 jours ouvrables en commandant avant 15h", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
