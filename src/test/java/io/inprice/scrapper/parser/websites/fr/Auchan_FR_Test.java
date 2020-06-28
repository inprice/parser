package io.inprice.scrapper.parser.websites.fr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Auchan_FR_Test {

    private final String SITE_NAME = "auchan";
    private final String COUNTRY_CODE = "fr";

    private final Auchan site = new Auchan(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("C1091807", competitor.getSku());
        assertEquals("Bio Lubrifiant Social Marchesseau Bourgueil Rouge 2017", competitor.getName());
        assertEquals("7.72", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("Auchan", competitor.getSeller());
        assertEquals("3,99 € Livraison en magasin estimée le 28/06/2019 Offert à partir de 25 €", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("M1406900", competitor.getSku());
        assertEquals("X-TRI2 YF910 pliable avec dossier", competitor.getName());
        assertEquals("179.00", competitor.getPrice().toString());
        assertEquals("TECNOVITA", competitor.getBrand());
        assertEquals("Rsi-fitworld", competitor.getSeller());
        assertEquals("Offert Livraison standard à domicile estimée le 28/06/2019", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("C1011762", competitor.getSku());
        assertEquals("MT 110 B - Platine vinyle vintage - Noir", competitor.getName());
        assertEquals("99.00", competitor.getPrice().toString());
        assertEquals("MUSE", competitor.getBrand());
        assertEquals("Auchan", competitor.getSeller());
        assertEquals("Offert Livraison standard en point retrait estimée le 02/07/2019", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("M2089818", competitor.getSku());
        assertEquals("Bonnet de bain Speedo Mouldede silicone kid red Rouge 83667", competitor.getName());
        assertEquals("3.99", competitor.getPrice().toString());
        assertEquals("SPEEDO", competitor.getBrand());
        assertEquals("Sports Depot", competitor.getSeller());
        assertEquals("6,90 € Livraison standard à domicile estimée le 28/06/2019", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
