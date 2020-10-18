package io.inprice.parser.websites.fr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CDiscount_FR_Test {

    private final String SITE_NAME = "cdiscount";
    private final String COUNTRY_CODE = "fr";

    private final CDiscount site = new CDiscount(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("dys5025155031513", link.getSku());
        assertEquals("DYSON Aspirateur balai V8 ABSOLUTE NEW", link.getName());
        assertEquals("379.99", link.getPrice().toString());
        assertEquals("DYSON", link.getBrand());
        assertEquals("Cdiscount", link.getSeller());
        assertEquals("Vendu et expédié par Cdiscount", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("bes6942138929485", link.getSku());
        assertEquals("BESTWAY Kit Piscine rectangulaire tubulaire L4,12 x l2,01 x H1,22m", link.getName());
        assertEquals("369.00", link.getPrice().toString());
        assertEquals("BESTWAY", link.getBrand());
        assertEquals("Cdiscount", link.getSeller());
        assertEquals("Vendu et expédié par Cdiscount", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("hai6901018042027", link.getSku());
        assertEquals("Haier HRF-629IF6  - Réfrigérateur américain - Total No Frost - 550L (375 + 175 L) - Distributeur glaçons -L 90,8 x H 179 cm- A+ Inox", link.getName());
        assertEquals("699.99", link.getPrice().toString());
        assertEquals("HAIER", link.getBrand());
        assertEquals("Cdiscount", link.getSeller());
        assertEquals("Vendu et expédié par Cdiscount", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("iphonex64gogs", link.getSku());
        assertEquals("APPLE iPhone X Gris Sidéral 64 Go", link.getName());
        assertEquals("779.99", link.getPrice().toString());
        assertEquals("APPLE", link.getBrand());
        assertEquals("idigital", link.getSeller());
        assertEquals("Vendu et expédié par idigital", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
