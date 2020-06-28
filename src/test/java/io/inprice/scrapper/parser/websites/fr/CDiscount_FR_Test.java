package io.inprice.scrapper.parser.websites.fr;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CDiscount_FR_Test {

    private final String SITE_NAME = "cdiscount";
    private final String COUNTRY_CODE = "fr";

    private final CDiscount site = new CDiscount(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("dys5025155031513", competitor.getSku());
        assertEquals("DYSON Aspirateur balai V8 ABSOLUTE NEW", competitor.getName());
        assertEquals("379.99", competitor.getPrice().toString());
        assertEquals("DYSON", competitor.getBrand());
        assertEquals("Cdiscount", competitor.getSeller());
        assertEquals("Vendu et expédié par Cdiscount", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("bes6942138929485", competitor.getSku());
        assertEquals("BESTWAY Kit Piscine rectangulaire tubulaire L4,12 x l2,01 x H1,22m", competitor.getName());
        assertEquals("369.00", competitor.getPrice().toString());
        assertEquals("BESTWAY", competitor.getBrand());
        assertEquals("Cdiscount", competitor.getSeller());
        assertEquals("Vendu et expédié par Cdiscount", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("hai6901018042027", competitor.getSku());
        assertEquals("Haier HRF-629IF6  - Réfrigérateur américain - Total No Frost - 550L (375 + 175 L) - Distributeur glaçons -L 90,8 x H 179 cm- A+ Inox", competitor.getName());
        assertEquals("699.99", competitor.getPrice().toString());
        assertEquals("HAIER", competitor.getBrand());
        assertEquals("Cdiscount", competitor.getSeller());
        assertEquals("Vendu et expédié par Cdiscount", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("iphonex64gogs", competitor.getSku());
        assertEquals("APPLE iPhone X Gris Sidéral 64 Go", competitor.getName());
        assertEquals("779.99", competitor.getPrice().toString());
        assertEquals("APPLE", competitor.getBrand());
        assertEquals("idigital", competitor.getSeller());
        assertEquals("Vendu et expédié par idigital", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
