package io.inprice.parser.websites.fr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Auchan_FR_Test {

    private final String SITE_NAME = "auchan";
    private final String COUNTRY_CODE = "fr";

    private final Auchan site = new Auchan();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("C1091807", link.getSku());
        assertEquals("Bio Lubrifiant Social Marchesseau Bourgueil Rouge 2017", link.getName());
        assertEquals("7.72", link.getPrice().toString());
        assertEquals(Consts.Words.NOT_AVAILABLE, link.getBrand());
        assertEquals("Auchan", link.getSeller());
        assertEquals("3,99 € Livraison en magasin estimée le 28/06/2019 Offert à partir de 25 €", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("M1406900", link.getSku());
        assertEquals("X-TRI2 YF910 pliable avec dossier", link.getName());
        assertEquals("179.00", link.getPrice().toString());
        assertEquals("TECNOVITA", link.getBrand());
        assertEquals("Rsi-fitworld", link.getSeller());
        assertEquals("Offert Livraison standard à domicile estimée le 28/06/2019", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("C1011762", link.getSku());
        assertEquals("MT 110 B - Platine vinyle vintage - Noir", link.getName());
        assertEquals("99.00", link.getPrice().toString());
        assertEquals("MUSE", link.getBrand());
        assertEquals("Auchan", link.getSeller());
        assertEquals("Offert Livraison standard en point retrait estimée le 02/07/2019", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("M2089818", link.getSku());
        assertEquals("Bonnet de bain Speedo Mouldede silicone kid red Rouge 83667", link.getName());
        assertEquals("3.99", link.getPrice().toString());
        assertEquals("SPEEDO", link.getBrand());
        assertEquals("Sports Depot", link.getSeller());
        assertEquals("6,90 € Livraison standard à domicile estimée le 28/06/2019", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}