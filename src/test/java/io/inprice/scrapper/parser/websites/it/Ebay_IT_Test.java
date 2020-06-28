package io.inprice.scrapper.parser.websites.it;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import io.inprice.scrapper.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_IT_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.scrapper.parser.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("172911378400", competitor.getSku());
        assertEquals("TAPIS ROULANT ELETTRICO PIEGHEVOLE SENSORE CARDIACO BLUETOOTH APP iFitShow", competitor.getName());
        assertEquals("199.99", competitor.getPrice().toString());
        assertEquals("yourmove", competitor.getBrand());
        assertEquals("rl-perfect-price", competitor.getSeller());
        assertEquals("Potrebbe non essere disponibile il servizio di spedizione verso: Turchia", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("292883343963", competitor.getSku());
        assertEquals("SMARTWATCH CARDIOFREQUENZIMETRO POLSO SPORT BLUETOOTH GPS CONTAPASSI CALORIE W5", competitor.getName());
        assertEquals("9.80", competitor.getPrice().toString());
        assertEquals("- Senza marca/Generico -", competitor.getBrand());
        assertEquals("wishingtree55", competitor.getSeller());
        assertEquals("Potrebbe non essere disponibile il servizio di spedizione verso: Turchia", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("113774671899", competitor.getSku());
        assertEquals("Smith MOTO CROSS OCCHIALI roll off non mai indossato in KTM Colori Rosso", competitor.getName());
        assertEquals("1.00", competitor.getPrice().toString());
        assertEquals("NA", competitor.getBrand());
        assertEquals("elsamia2016", competitor.getSeller());
        assertEquals("EUR 10,99 Standard", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("281732849736", competitor.getSku());
        assertEquals("NUOVI Pantaloncini Uomo Nike Logo T-shirt, top-Retrò Vintage con logo SPORT COTONE", competitor.getName());
        assertEquals("6.74", competitor.getPrice().toString());
        assertEquals("Nike", competitor.getBrand());
        assertEquals("empire_sports", competitor.getSeller());
        assertEquals("GBP 9,99 (circa EUR 11,24) Spedizione celere", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
