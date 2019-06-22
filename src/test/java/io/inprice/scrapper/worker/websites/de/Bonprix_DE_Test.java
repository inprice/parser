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
public class Bonprix_DE_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("96091695_27838420", link.getSku());
        assertEquals("Solarleuchte \"Leuchtturm\"", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("92556195_29973862", link.getSku());
        assertEquals("Brazil Slip (2er-Pack)", link.getName());
        assertEquals("14.98", link.getPrice().toString());
        assertEquals("BODYFLIRT", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("95378681_29519547", link.getSku());
        assertEquals("Outdoor Boot", link.getName());
        assertEquals("29.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("95016895_29852337", link.getSku());
        assertEquals("Mädchen Shirtkleid mit Volant", link.getName());
        assertEquals("12.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("5,99 € pro Bestellung zzgl. 23,99 € Großstückaufschlag Weitere Informationen >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}