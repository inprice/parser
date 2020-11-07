package io.inprice.parser.websites.nl;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_NL_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.parser.websites.xx.Ebay();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("311777636467", link.getSku());
        assertEquals("Portable Travel 110lb / 50kg LCD Digital Hanging Luggage Scale Electronic Weight", link.getName());
        assertEquals("7.93", link.getPrice().toString());
        assertEquals("Fosmon", link.getBrand());
        assertEquals("sfplanet", link.getSeller());
        assertEquals("Gratis USPS First Class Mail International / First Class Package International Service", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("223526308937", link.getSku());
        assertEquals("Blade INDUCTRIX FPV Drone", link.getName());
        assertEquals("67.50", link.getPrice().toString());
        assertEquals("Blade", link.getBrand());
        assertEquals("onlycoolrc", link.getSeller());
        assertEquals("GBP 14,99 (ongeveer EUR 16,87) Voordelige verzendservice", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("332864473404", link.getSku());
        assertEquals("LAMPIONE STRADALE FARO FARETTO AD ENERGIA SOLARE A LED FOTOVOLTAICO LUCE LED", link.getName());
        assertEquals("22.99", link.getPrice().toString());
        assertEquals("Shopping in rete", link.getBrand());
        assertEquals("marlyn_shop", link.getSeller());
        assertEquals("EUR 29,90 Spedizione internazionale espressa", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("192924322253", link.getSku());
        assertEquals("Outil horloger ancien horloge XIX eme clock Morez Morbier Comtoise Watchmakers 5", link.getName());
        assertEquals("118.00", link.getPrice().toString());
        assertEquals(Consts.Words.NOT_AVAILABLE, link.getBrand());
        assertEquals("beffroi111", link.getSeller());
        assertEquals("EUR 18,00 La Poste - Lettre Prioritaire Internationale", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}