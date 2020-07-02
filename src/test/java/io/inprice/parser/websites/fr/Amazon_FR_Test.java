package io.inprice.parser.websites.fr;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_FR_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "fr";

    private final Website site = new io.inprice.parser.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07G8CVQNK", competitor.getSku());
        assertEquals("Igloohome| Smart KeyBox 2 | Boîte à Clé Sécurisée Intelligente | PIN et Bluetooth | Noir", competitor.getName());
        assertEquals("169.00", competitor.getPrice().toString());
        assertEquals("Igloohome", competitor.getBrand());
        assertEquals("Igloohome France", competitor.getSeller());
        assertEquals("Livraison GRATUITE en France métropolitaine. Détails", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B007C26M0Q", competitor.getSku());
        assertEquals("Boss Boutons de Manchette 50219288-001", competitor.getName());
        assertEquals("54.00", competitor.getPrice().toString());
        assertEquals("Boss", competitor.getBrand());
        assertEquals("LORD OF LABEL", competitor.getSeller());
        assertEquals("Livraison GRATUITE en France métropolitaine. Détails", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07NSH38J6", competitor.getSku());
        assertEquals("DIDAR Projecteur 3500 Lumens Vidéoprojecteur Soutien HD1080P Portable Retroprojecteur, Multimédia Home Cinéma Full HD Pordinateur, Compatible avec HDMI VGA AV SD USB, Home Théâtre Projecteur", competitor.getName());
        assertEquals("64.59", competitor.getPrice().toString());
        assertEquals("DIDAR", competitor.getBrand());
        assertEquals("Didaronline", competitor.getSeller());
        assertEquals("Livraison GRATUITE en France métropolitaine. Détails", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B071438H2B", competitor.getSku());
        assertEquals("Pourvu", competitor.getName());
        assertEquals("6.99", competitor.getPrice().toString());
        assertEquals("Fontana", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Livraison gratuite dès EUR 25 d'achats en France métropolitaine. Détails", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
