package io.inprice.parser.websites.fr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.*;

public class Fnac_FR_Test {

    private final String SITE_NAME = "fnac";
    private final String COUNTRY_CODE = "fr";

    private final Fnac site = new Fnac(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("6837425", link.getSku());
        assertEquals("LEGO Creator 10220 Le campingcar Volkswagen T1", link.getName());
        assertEquals("81.99", link.getPrice().toString());
        assertEquals("Lego", link.getBrand());
        assertEquals("FNAC.COM", link.getSeller());
        assertEquals("Livraison gratuite", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("9116585", link.getSku());
        assertEquals("Blender Moulinex Freshboost LM181D10 800 W Noir", link.getName());
        assertEquals("99.99", link.getPrice().toString());
        assertEquals("Moulinex", link.getBrand());
        assertEquals("FNAC.COM", link.getSeller());
        assertEquals("Livraison gratuite", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("8598118", link.getSku());
        assertEquals("Casque Parrot Zik 3 by Starck Vert Emeraude avec chargeur à induction", link.getName());
        assertEquals("99.99", link.getPrice().toString());
        assertEquals("Parrot", link.getBrand());
        assertEquals("FNAC.COM", link.getSeller());
        assertEquals("Livraison à partir de 4€99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("8993319", link.getSku());
        assertEquals("Ventilateur Rowenta VU6620F0", link.getName());
        assertEquals("109.26", link.getPrice().toString());
        assertEquals("Rowenta", link.getBrand());
        assertEquals("LaBoutiqueDuNet", link.getSeller());
        assertEquals("Suivi : gratuit", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
