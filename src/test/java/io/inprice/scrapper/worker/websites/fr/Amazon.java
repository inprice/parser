package io.inprice.scrapper.worker.websites.fr;

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
public class Amazon {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B07G8CVQNK", link.getSku());
        assertEquals("Igloohome| Smart KeyBox 2 | Boîte à Clé Sécurisée Intelligente | PIN et Bluetooth | Noir", link.getName());
        assertEquals("169.00", link.getPrice().toString());
        assertEquals("Igloohome", link.getBrand());
        assertEquals("Igloohome France", link.getSeller());
        assertEquals("Livraison GRATUITE en France métropolitaine. Détails", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B007C26M0Q", link.getSku());
        assertEquals("Boss Boutons de Manchette 50219288-001", link.getName());
        assertEquals("54.00", link.getPrice().toString());
        assertEquals("Boss", link.getBrand());
        assertEquals("LORD OF LABEL", link.getSeller());
        assertEquals("Livraison GRATUITE en France métropolitaine. Détails", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B07NSH38J6", link.getSku());
        assertEquals("DIDAR Projecteur 3500 Lumens Vidéoprojecteur Soutien HD1080P Portable Retroprojecteur, Multimédia Home Cinéma Full HD Pordinateur, Compatible avec HDMI VGA AV SD USB, Home Théâtre Projecteur", link.getName());
        assertEquals("64.59", link.getPrice().toString());
        assertEquals("DIDAR", link.getBrand());
        assertEquals("Didaronline", link.getSeller());
        assertEquals("Livraison GRATUITE en France métropolitaine. Détails", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("B071438H2B", link.getSku());
        assertEquals("Pourvu", link.getName());
        assertEquals("6.99", link.getPrice().toString());
        assertEquals("Fontana", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Livraison gratuite dès EUR 25 d'achats en France métropolitaine. Détails", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}