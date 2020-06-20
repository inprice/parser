package io.inprice.scrapper.worker.websites.fr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Fnac_FR_Test {

    private final String SITE_NAME = "fnac";
    private final String COUNTRY_CODE = "fr";

    private final Fnac site = new Fnac(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("6837425", competitor.getSku());
        assertEquals("LEGO Creator 10220 Le campingcar Volkswagen T1", competitor.getName());
        assertEquals("81.99", competitor.getPrice().toString());
        assertEquals("Lego", competitor.getBrand());
        assertEquals("FNAC.COM", competitor.getSeller());
        assertEquals("Livraison gratuite", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("9116585", competitor.getSku());
        assertEquals("Blender Moulinex Freshboost LM181D10 800 W Noir", competitor.getName());
        assertEquals("99.99", competitor.getPrice().toString());
        assertEquals("Moulinex", competitor.getBrand());
        assertEquals("FNAC.COM", competitor.getSeller());
        assertEquals("Livraison gratuite", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("8598118", competitor.getSku());
        assertEquals("Casque Parrot Zik 3 by Starck Vert Emeraude avec chargeur à induction", competitor.getName());
        assertEquals("99.99", competitor.getPrice().toString());
        assertEquals("Parrot", competitor.getBrand());
        assertEquals("FNAC.COM", competitor.getSeller());
        assertEquals("Livraison à partir de 4€99", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("8993319", competitor.getSku());
        assertEquals("Ventilateur Rowenta VU6620F0", competitor.getName());
        assertEquals("109.26", competitor.getPrice().toString());
        assertEquals("Rowenta", competitor.getBrand());
        assertEquals("LaBoutiqueDuNet", competitor.getSeller());
        assertEquals("Suivi : gratuit", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
