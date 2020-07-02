package io.inprice.parser.websites.de;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.*;

public class Rakuten_DE_Test {

    private final String SITE_NAME = "rakuten";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.parser.websites.xx.Rakuten(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("56477395", competitor.getSku());
        assertEquals("Western Stars", competitor.getName());
        assertEquals("20.99", competitor.getPrice().toString());
        assertEquals("Sony Music Entertainment Germ", competitor.getBrand());
        assertEquals("buecher.de", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("217988201", competitor.getSku());
        assertEquals("Xiaomi N4M340 Ninebot Plus 11 Zoll Electric Scooter Self Balancing Selbstbalancierendes Doppelräder", competitor.getName());
        assertEquals("707.77", competitor.getPrice().toString());
        assertEquals("Xiaomi", competitor.getBrand());
        assertEquals("Shenzhen Shanghua E-Commerce Co", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("1353202652-6613-1937", competitor.getSku());
        assertEquals("Energiespar Deckenventilator Eco Genuino Chrom Flügel Holz Natur", competitor.getName());
        assertEquals("489.00", competitor.getPrice().toString());
        assertEquals("CasaFan", competitor.getBrand());
        assertEquals("Tobias Krist", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("00000182-500", competitor.getSku());
        assertEquals("Eisformen aus Silikon / Stieleisformen / Eisformen Eis am Stiel, Stieleisformen Silikon, rot", competitor.getName());
        assertEquals("16.99", competitor.getPrice().toString());
        assertEquals("Zollner24", competitor.getBrand());
        assertEquals("Zollner24", competitor.getSeller());
        assertEquals("Free shipping", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
