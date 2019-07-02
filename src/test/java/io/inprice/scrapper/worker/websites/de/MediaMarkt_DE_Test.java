package io.inprice.scrapper.worker.websites.de;

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
public class MediaMarkt_DE_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.de.MediaMarkt(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
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

        assertEquals(Status.AVAILABLE, link.getStatus());
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

        assertEquals(Status.AVAILABLE, link.getStatus());
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

        assertEquals(Status.OUT_OF_STOCK, link.getStatus());
        assertEquals("2519798", link.getSku());
        assertEquals("Kiss - Kissworld-The Best Of Kiss (2LP) [Vinyl]", link.getName());
        assertEquals("22.99", link.getPrice().toString());
        assertEquals("MERCURY", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Versandkosten 1.99", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
