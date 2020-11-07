package io.inprice.parser.websites.de;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_DE_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.parser.websites.xx.Zalando();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("TIB31H01D-S11", link.getSku());
        assertEquals("BED HEAD AFTER PARTY 100ML - Styling", link.getName());
        assertEquals("16.05", link.getPrice().toString());
        assertEquals("Tigi", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("ES183C02R-K11", link.getSku());
        assertEquals("BRAVA BEACH AMERICAN NECKHOLDER HIPSTER - Bikini", link.getName());
        assertEquals("27.95", link.getPrice().toString());
        assertEquals("Esprit", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("GU152M00V-D11", link.getSku());
        assertEquals("MENS SPORT - Uhr - silver", link.getName());
        assertEquals("189.95", link.getPrice().toString());
        assertEquals("Guess", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("K4451K00T-Q11", link.getSku());
        assertEquals("Sonnenbrille", link.getName());
        assertEquals("12.75", link.getPrice().toString());
        assertEquals("KIOMI", link.getBrand());
        assertEquals("Zalando", link.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}