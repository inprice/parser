package io.inprice.parser.websites.de;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaMarkt_DE_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.parser.websites.de.MediaMarkt();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("2251315", link.getSku());
        assertEquals("LELO EARL GOLD Analplug", link.getName());
        assertEquals("1399.00", link.getPrice().toString());
        assertEquals("LELO", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Kostenloser Versand", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1255714", link.getSku());
        assertEquals("KOPP 120913004 TSD Steckdosenleiste", link.getName());
        assertEquals("4.99", link.getPrice().toString());
        assertEquals("KOPP", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Versandkosten 1.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("2183219", link.getSku());
        assertEquals("GOPRO 3661-164, Dualladegerät + Akku, GoPro HERO5 Black, GoPro HERO6 Black, GoPro Hero7 Black, Schwarz", link.getName());
        assertEquals("57.99", link.getPrice().toString());
        assertEquals("GOPRO", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Versandkosten 1.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.NOT_AVAILABLE, link.getStatus());
        assertEquals("2519798", link.getSku());
        assertEquals("Kiss - Kissworld-The Best Of Kiss (2LP) [Vinyl]", link.getName());
        assertEquals("22.99", link.getPrice().toString());
        assertEquals("MERCURY", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Versandkosten 1.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_5() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("2586325", link.getSku());
        assertEquals("SAMSUNG Galaxy Tab A 10.1 Wi-Fi, Tablet, 64 GB, Nein, 10,1 Zoll, Black", link.getName());
        assertEquals("239.99", link.getPrice().toString());
        assertEquals("SAMSUNG", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Lieferung 17.06.2020 - 18.06.2020 + €0.00", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
