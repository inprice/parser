package io.inprice.scrapper.worker.websites.nl;

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
public class Bonprix_NL_Test {

    private final String SITE_NAME = "bonprix";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Bonprix(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.UNAVAILABLE, link.getStatus());
        assertEquals("97447795_29806865", link.getSku());
        assertEquals("Sweatbermuda", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("bpc bonprix collection", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 4,95 per bestelling Meer informatie >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("94977395_26495747", link.getSku());
        assertEquals("Dekbedovertrek «Aap»", link.getName());
        assertEquals("19.99", link.getPrice().toString());
        assertEquals("bpc living", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 4,95 per bestelling Meer informatie >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("90555595_26598631", link.getSku());
        assertEquals("Halskettingen (3-dlg. set)", link.getName());
        assertEquals("12.99", link.getPrice().toString());
        assertEquals("RAINBOW", link.getBrand());
        assertEquals("Bonprix", link.getSeller());
        assertEquals("€ 4,95 per bestelling Meer informatie >", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
