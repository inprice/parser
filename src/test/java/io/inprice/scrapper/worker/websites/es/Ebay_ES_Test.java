package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Ebay_ES_Test {

    private final String SITE_NAME = "ebay";
    private final String COUNTRY_CODE = "es";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Ebay(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("264218368315", competitor.getSku());
        assertEquals("ORDENADOR PORTATIL LENOVO INTEL 4GB 500GB WIFI WINDOWS 10 + OFFICE +ANTIVIRUS", competitor.getName());
        assertEquals("205.95", competitor.getPrice().toString());
        assertEquals("Lenovo", competitor.getBrand());
        assertEquals("boxterass", competitor.getSeller());
        assertEquals("No se realizan envíos a Turquía", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("202113774000", competitor.getSku());
        assertEquals("Taladro Atornillador a Bateria 18V Litio sin Cable con Maletin", competitor.getName());
        assertEquals("34.99", competitor.getPrice().toString());
        assertEquals("T-LoVendo", competitor.getBrand());
        assertEquals("t-lovendo_com", competitor.getSeller());
        assertEquals("No se puede enviar a Turquía", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("202346134548", competitor.getSku());
        assertEquals("Silla Gaming Oficina Racing Sillon gamer Despacho Profesional Videojuegos PC nue", competitor.getName());
        assertEquals("89.99", competitor.getPrice().toString());
        assertEquals("T-LoVendo", competitor.getBrand());
        assertEquals("t-lovendo_com", competitor.getSeller());
        assertEquals("No se puede enviar a Turquía", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("323291864624", competitor.getSku());
        assertEquals("iRobot Roomba 696 robot aspirador sin bolsa", competitor.getName());
        assertEquals("229.99", competitor.getPrice().toString());
        assertEquals("iRobot", competitor.getBrand());
        assertEquals("ofertas3b", competitor.getSeller());
        assertEquals("No se puede enviar a Turquía", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
