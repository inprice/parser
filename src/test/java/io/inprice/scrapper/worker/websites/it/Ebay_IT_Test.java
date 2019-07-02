package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class Ebay_IT_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("172911378400", link.getSku());
        assertEquals("TAPIS ROULANT ELETTRICO PIEGHEVOLE SENSORE CARDIACO BLUETOOTH APP iFitShow", link.getName());
        assertEquals("199.99", link.getPrice().toString());
        assertEquals("yourmove", link.getBrand());
        assertEquals("rl-perfect-price", link.getSeller());
        assertEquals("Potrebbe non essere disponibile il servizio di spedizione verso: Turchia", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("292883343963", link.getSku());
        assertEquals("SMARTWATCH CARDIOFREQUENZIMETRO POLSO SPORT BLUETOOTH GPS CONTAPASSI CALORIE W5", link.getName());
        assertEquals("9.80", link.getPrice().toString());
        assertEquals("- Senza marca/Generico -", link.getBrand());
        assertEquals("wishingtree55", link.getSeller());
        assertEquals("Potrebbe non essere disponibile il servizio di spedizione verso: Turchia", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("113774671899", link.getSku());
        assertEquals("Smith MOTO CROSS OCCHIALI roll off non mai indossato in KTM Colori Rosso", link.getName());
        assertEquals("1.00", link.getPrice().toString());
        assertEquals("NA", link.getBrand());
        assertEquals("elsamia2016", link.getSeller());
        assertEquals("EUR 10,99 Standard", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("281732849736", link.getSku());
        assertEquals("NUOVI Pantaloncini Uomo Nike Logo T-shirt, top-Retrò Vintage con logo SPORT COTONE", link.getName());
        assertEquals("6.74", link.getPrice().toString());
        assertEquals("Nike", link.getBrand());
        assertEquals("empire_sports", link.getSeller());
        assertEquals("GBP 9,99 (circa EUR 11,24) Spedizione celere", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
