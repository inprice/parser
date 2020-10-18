package io.inprice.parser.websites.nl;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MediaMarkt_NL_Test {

    private final String SITE_NAME = "mediamarkt";
    private final String COUNTRY_CODE = "nl";

    private final Website site = new io.inprice.parser.websites.xx.MediaMarkt(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1070699", link.getSku());
        assertEquals("HAMA USB 2.0 verlengkabel 1 ster 0,25m", link.getName());
        assertEquals("6.99", link.getPrice().toString());
        assertEquals("HAMA", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Gratis bezorging vanaf € 20", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1555572", link.getSku());
        assertEquals("ZANUSSI ZGH65414XS", link.getName());
        assertEquals("299.00", link.getPrice().toString());
        assertEquals("ZANUSSI", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Gratis bezorging", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1484859", link.getSku());
        assertEquals("LOGITECH POP Add-on Home Switch Grijs", link.getName());
        assertEquals("29.00", link.getPrice().toString());
        assertEquals("LOGITECH", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Incl. btw excl. verzendkosten", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("1399691", link.getSku());
        assertEquals("Sarah McLachlan - Surfacing | Vinyl", link.getName());
        assertEquals("22.99", link.getPrice().toString());
        assertEquals("BERTUS DISTRIBUTIE BERT", link.getBrand());
        assertEquals("Media Markt", link.getSeller());
        assertEquals("Gratis bezorging", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
